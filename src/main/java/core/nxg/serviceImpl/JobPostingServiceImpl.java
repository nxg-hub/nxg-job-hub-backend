package core.nxg.serviceImpl;

import core.nxg.dto.JobPostingDto;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.Employer;
import core.nxg.entity.JobPosting;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.service.EmailService;
import core.nxg.service.JobPostingService;
import core.nxg.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPostingServiceImpl implements JobPostingService {

    @Autowired
    private final JobPostingRepository jobPostingRepository;
    @Autowired
    private final EmployerRepository employerRepository;
    @Autowired
    private final EmailService emailService;
    @Autowired
    private final UserService userService;

    @Autowired
    private final ModelMapper mapper;


    @Override
    public List<JobPostingDto> getAllJobPostings(Pageable pageable) {
        List<JobPosting> jobPostings = jobPostingRepository.findAll();
        return jobPostings.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception {


        JobPosting jobPosting = new JobPosting();
        String employerId = jobPostingDto.getEmployerID();
        Optional<Employer> optionalEmployer = employerRepository.findById(Long.valueOf(employerId));
        if (optionalEmployer.isPresent()) {
            jobPosting.setEmployerID(String.valueOf(optionalEmployer.get().getEmployerID()));
        } else {
            throw new NotFoundException("Employer does not exist!");
        }
        jobPosting.setJob_description(jobPostingDto.getJob_description());
        jobPosting.setJob_title(jobPostingDto.getJob_title());
        jobPosting.setJob_type(jobPostingDto.getJob_type());
        jobPosting.setJob_location(jobPostingDto.getJob_location());
        jobPosting.setSalary(jobPostingDto.getSalary());
        jobPosting.setJob_location(jobPostingDto.getJob_location());
        jobPosting.setRequirements(jobPostingDto.getRequirements());
        jobPosting.setDeadline(jobPostingDto.getDeadline());
        jobPosting.setTags(jobPostingDto.getTags());
        jobPosting.setCompany_bio(jobPostingDto.getCompany_bio());
        var savedJobPosting = jobPostingRepository.saveAndFlush(jobPosting);
//        onJobPosted(Long.valueOf(employerId), savedJobPosting);
        return mapper.map(savedJobPosting, JobPostingDto.class);
    }

    private void onJobPosted(Long employerId, JobPosting jobPosting) throws Exception {
        //TODO send email to only subscribed tech talent agents and tech talent
        Employer poster = employerRepository.findById(employerId).
                orElseThrow(() -> new NotFoundException("Employer was not found!"));

        Page<UserResponseDto> users = userService.getAllUsers(Pageable.unpaged());

        users.forEach(user -> {
            final String posterName = poster.getCompanyName();
            try {
                log.info("Preparing to send an email to {}", user.getEmail());

                emailService.sendJobPostingNotifEmail(user.getEmail(), jobPosting);

                log.info("Email sent to {}", user.getEmail());
            } catch (Exception e) {
                throw new RuntimeException("Error sending email to {}" + user.getEmail());
            }
        });


    }

    @Override
    public JobPostingDto getJobPostingById(Long jobId) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findJobPostingByJobID(jobId);
        if (optionalJobPosting.isPresent()) {
            return mapToDto(optionalJobPosting.get());
        } else {
            throw new NotFoundException("Job posting with ID " + jobId + " not found");
        }
    }


    @Override
    public JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findJobPostingByJobID(jobId);
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
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findJobPostingByJobID(jobId);
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



    private List<JobPosting> getJobPostingsForEvents(){
        return  jobPostingRepository.findAll() ;
    }

    @Override
    public Flux<ServerSentEvent<List<JobPosting>>> sendJobPostingEvents() throws InterruptedException {

        var postings = getJobPostingsForEvents();
        if (!postings.isEmpty()) {
            return Flux.interval(Duration.ofSeconds(4))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<JobPosting>>builder().id(String.valueOf(sequence))
                            .event("jobpostings")
                            .data(
                                    (getJobPostingsForEvents()))
                            .comment("A new job posting event")
                            .build());
        }
            return Flux.interval(Duration.ofSeconds(3))
                    .map(sequence -> ServerSentEvent.<List<JobPosting>>builder().id(String.valueOf(sequence))
                            .event("jobpostings").data(new ArrayList<>()).build());
        }

}
