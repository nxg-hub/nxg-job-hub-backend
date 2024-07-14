package core.nxg.serviceImpl;

import core.nxg.algorithm.JobAlgorithmByContent;
import core.nxg.dto.JobPostingDto;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.*;
import core.nxg.enums.NotificationType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.NotificationRepository;
import core.nxg.repository.TechTalentRepository;
import core.nxg.service.EmailService;
import core.nxg.service.JobPostingService;
import core.nxg.service.UserService;
import core.nxg.subscription.entity.Subscriber;
import core.nxg.subscription.enums.PlanType;
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    private final JobAlgorithmByContent algorithmByContent;

    @Autowired
    private final JobPostingRepository jobPostingRepository;

    @Autowired
    private final EmployerRepository employerRepository;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final TechTalentRepository techRepo;

    @Autowired
    private final SubscribeRepository subRepo;
    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final ModelMapper mapper;

    private final GeonamesService geonamesService;


    @Override
    public List<JobPostingDto> getAllJobPostings(Pageable pageable) {
        List<JobPosting> jobPostings = jobPostingRepository.findAll();
        return jobPostings.stream().map(this::mapToDto).collect(Collectors.toList());
        // replicate this methode.
    }


    @Override
    public JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception {

        JobPosting jobPosting = new JobPosting();
        String employerId = jobPostingDto.getEmployerID();
        Optional<Employer> optionalEmployer = employerRepository.findById(employerId);
        if (optionalEmployer.isEmpty()) {
            throw new NotFoundException("Employer does not exist!");
        }

        Employer employer = optionalEmployer.get();

        if (!employer.isVerified()) {
            throw new RuntimeException("Employer is not verified. Job posting cannot be created");
        }

        // Check if the employer is within their free one-month period
        LocalDateTime accountCreationDate = employer.getAccountCreationDate(); // assuming getAccountCreationDate() returns account creation date
        LocalDateTime oneMonthLater = accountCreationDate.plusMonths(1);

        if (LocalDateTime.now().isAfter(oneMonthLater)) {
            Optional<Subscriber> optionalSubscription = subRepo.findByEmail(employer.getEmail());
            Subscriber subscription;
            if (optionalSubscription.isEmpty()) {
                // Create a default subscription if not exists
                subscription = new Subscriber();
                subscription.setEmail(employer.getEmail());
                subscription.setPlanType(PlanType.FREE); // or any default plan type
                subscription.setSubscriptionStarts(LocalDate.now());
                subscription.setSubscriptionDues(LocalDate.now().plusMonths(1)); // 1 month free period
                subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE); // initial status
                subscription.setUser(employer.getUser());
                subscription = subRepo.save(subscription);
            } else {
                subscription = optionalSubscription.get();
            }

            if (SubscriptionStatus.INACTIVE.equals(subscription.getSubscriptionStatus())) {
                throw new RuntimeException("Employer subscription is inactive. Job posting cannot be created");
            }
        }


        jobPosting.setEmployerID(String.valueOf(optionalEmployer.get().getEmployerID()));
        jobPosting.setJob_description(jobPostingDto.getJob_description());
        jobPosting.setEmployer_name(optionalEmployer.get().getCompanyName());
        jobPosting.setEmployer_profile_pic(optionalEmployer.get().getUser().getProfilePicture());
        jobPosting.setJob_title(jobPostingDto.getJob_title());
        jobPosting.setJob_type(jobPostingDto.getJob_type());
        jobPosting.setJob_location(jobPostingDto.getJob_location());
        jobPosting.setSalary(jobPostingDto.getSalary());
        jobPosting.setJob_location(jobPostingDto.getJob_location());
        jobPosting.setRequirements(jobPostingDto.getRequirements());
        jobPosting.setDeadline(jobPostingDto.getDeadline());
        jobPosting.setCreatedAt(jobPosting.getCreatedAt());
        jobPosting.setTags(jobPostingDto.getTags());
        jobPosting.setCompany_bio(jobPostingDto.getCompany_bio());

        var savedJobPosting = jobPostingRepository.save(jobPosting);
        onJobPosted(employerId, savedJobPosting);
        return mapper.map(savedJobPosting, JobPostingDto.class);
    }


    private void notify(User subscriber,JobPosting jobPosting, User sender){

        var notification = Notification.builder()
                .notificationType(NotificationType.JOB_POST)
                .delivered(false)
                .message(jobPosting.getJob_title())
                .contentId(jobPosting.getJobID())
                .referencedUserID(subscriber.getId())
                .senderID(sender.getId())
                .dateTime(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

    }


    private void onJobPosted(String employerId, JobPosting jobPosting) throws Exception {
        Employer poster = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer was not found!"));

        List<TechTalentUser> users = techRepo.findAll();

        // Filter users based on partial or full match of their interest in the job title
        List<TechTalentUser> interestedUsers = users.stream()
                .filter(user -> {
                    if (user.getJobInterest() == null) {
                        return false;
                    }
                    String jobInterest = user.getJobInterest().toLowerCase();
                    String jobTitle = jobPosting.getJob_title().toLowerCase();

                    // Check if any word from job interest is in the job title
                    String[] interestWords = jobInterest.split("\\s+");
                    boolean anyWordMatch = Arrays.stream(interestWords)
                            .anyMatch(word -> jobTitle.contains(word));

                    // Check if job interest is a substring of the job title
                    boolean substringMatch = jobTitle.contains(jobInterest);

                    return anyWordMatch || substringMatch;
                })
                .collect(Collectors.toList());

        for (TechTalentUser user : interestedUsers) {
            try {
                log.info("Preparing to send an email to {}", user.getEmail());

                emailService.sendJobRelatedNotifEmail(user.getEmail(), jobPosting);

                notify(user.getUser(), jobPosting, poster.getUser());

                log.info("Email notification sent to {}", user.getEmail());

            } catch (Exception e) {
                log.error("Error sending email to {}", user.getEmail(), e);
            }
        }

        emailService.sendJobRelatedNotifEmail(poster.getEmail(), jobPosting);
    }


    @Override
    public JobPostingDto getJobPostingById(Long jobId) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findById(String.valueOf(jobId));
        if (optionalJobPosting.isPresent()) {
            return mapToDto(optionalJobPosting.get());
        } else {
            throw new NotFoundException("Job posting with ID " + jobId + " not found");
        }
    }


    @Override
    public JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findById(String.valueOf(jobId));
        if (optionalJobPosting.isPresent()) {
            JobPosting existingJobPosting = optionalJobPosting.get();

            BeanUtils.copyProperties(jobPostingDto, existingJobPosting);
            JobPosting updatedJobPosting = jobPostingRepository.save(existingJobPosting);

            return mapToDto(updatedJobPosting);
        } else {
            throw new NotFoundException("Job posting with ID " + jobId + " not found");
        }
    }


    @Override
    public void deleteJobPosting(Long jobId) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findById(String.valueOf(jobId));
        if (optionalJobPosting.isPresent()) {
            JobPosting jobPosting = optionalJobPosting.get();
            jobPostingRepository.delete(jobPosting);
        } else {
            throw new NotFoundException("Job posting with ID " + jobId + " not found");
        }
    }


    private JobPostingDto mapToDto(JobPosting jobPosting) {
        JobPostingDto jobPostingDto = new JobPostingDto();
        BeanUtils.copyProperties(jobPosting, jobPostingDto);
        return jobPostingDto;
    }



    @Async
    public CompletableFuture<List<JobPosting>> getJobPostingsForEvents() {
        return CompletableFuture.supplyAsync(() -> {
            var toBeDelivered = jobPostingRepository.findByDeliveredFalse();
            toBeDelivered.forEach(a -> a.setDelivered(true));
            jobPostingRepository.saveAll(toBeDelivered);
            return toBeDelivered;
        });
        }
   @Override
    public Flux<ServerSentEvent<CompletableFuture<List<JobPosting>>>> sendJobPostingEvents() throws InterruptedException {
            return Flux.interval(Duration.ofSeconds(5))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<CompletableFuture<List<JobPosting>>>builder().id(String.valueOf(sequence))
                            .event("jobpostings")
                            .data(
                                   this.getJobPostingsForEvents())
                            .comment("A new job posting event")
                            .build());

        }


     @Override
    public Object recommendJobPosting(String userID) throws Exception{

        UserResponseDto userR = userService.getUserById(userID);

        User  user  = mapper.map(userR, User.class);


        return algorithmByContent.getJobs(user);

     }



    @Override
    public List<JobPosting> getNearbyJobPostings(String userCity) {
        var jobPostings = jobPostingRepository.findAll();

        // Get the list of nearby cities
        List<String> nearbyCities = geonamesService.getNearbyCities(userCity);
        nearbyCities.add(userCity); // Include the user's city itself

        // Filter job postings by city
        return jobPostings.stream()
                .filter(job -> nearbyCities.contains(job.getJob_location()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<JobPosting> getRecentJobPostings(int page, int size) {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        return jobPostingRepository.findByCreatedAtAfter(twoDaysAgo, PageRequest.of(page, size));
    }


}
