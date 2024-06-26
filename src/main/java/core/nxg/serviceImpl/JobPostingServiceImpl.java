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
import core.nxg.subscription.enums.SubscriptionStatus;
import core.nxg.subscription.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
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
    }

    @Override
    public JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception {


        JobPosting jobPosting = new JobPosting();
        String employerId = jobPostingDto.getEmployerID();
        Optional<Employer> optionalEmployer = employerRepository.findById(employerId);
        if (optionalEmployer.isEmpty()) {
            throw new NotFoundException("Employer does not exist!");

        }
        if (!optionalEmployer.get().isVerified()){
            throw new RuntimeException("Employer is not verified. Job posting cannot be created");
        }


       var subscription = subRepo.findByEmail(optionalEmployer.get().getEmail());


        if (subscription.isPresent() && (SubscriptionStatus.INACTIVE.equals( subscription.get().getSubscriptionStatus()))){
            throw new RuntimeException("Employer subscription is inactive. Job posting cannot be created");
        }

        jobPosting.setEmployerID(String.valueOf(optionalEmployer.get().getEmployerID()));
        jobPosting.setJob_description(jobPostingDto.getJob_description());
        jobPosting.setJob_title(jobPostingDto.getJob_title());
        jobPosting.setJob_type(jobPostingDto.getJob_type());
        jobPosting.setJob_location(jobPostingDto.getJob_location());
        jobPosting.setJob_description(jobPostingDto.getJob_description());// created new by glory
        jobPosting.setSalary(jobPostingDto.getSalary());
        jobPosting.setJob_location(jobPostingDto.getJob_location());
        jobPosting.setRequirements(jobPostingDto.getRequirements());
        jobPosting.setDeadline(jobPostingDto.getDeadline());
        jobPosting.setCreated_at(jobPosting.getCreated_at());// created new by glory
        jobPosting.setTags(jobPostingDto.getTags());
        jobPosting.setCompany_bio(jobPostingDto.getCompany_bio());
        var savedJobPosting = jobPostingRepository.save(jobPosting);
        onJobPosted(Long.valueOf(employerId), savedJobPosting);
        return mapper.map(savedJobPosting, JobPostingDto.class);
    }

    private void notify(User subscriber,JobPosting jobPosting, User sender){

        var notification = Notification.builder()
                .notificationType(NotificationType.JOB_POST)
                .delivered(false)
                .message(jobPosting.getJob_title())
                .contentId(Long.valueOf(jobPosting.getJobID()))
                .referencedUserID(subscriber.getId())
                .senderID(sender.getId())
                .dateTime(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);

    }


    private void onJobPosted(Long employerId, JobPosting jobPosting) throws Exception {
        Employer poster = employerRepository.findById(String.valueOf(employerId)).
                orElseThrow(() -> new NotFoundException("Employer was not found!"));

        List<TechTalentUser> users = techRepo.findAll();

        users.forEach(user -> {
            try {
                log.info("Preparing to send an email to {}", user.getEmail());

                emailService.sendJobRelatedNotifEmail(user.getEmail(), jobPosting);

                notify(user.getUser(), jobPosting, poster.getUser());

                log.info("Email notification sent to {}", user.getEmail());

            } catch (Exception e) {

                throw new RuntimeException("Error sending email to {}" + user.getEmail());
            }
        });
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


}
