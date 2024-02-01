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
        jobPosting.setCreated_at(LocalDate.now());
        String employerId = jobPostingDto.getEmployerID();
//        BeanUtils.copyProperties(jobPostingDto, jobPosting);
        JobPosting mapped = mapper.map(jobPostingDto, JobPosting.class);
        JobPosting savedJobPosting = jobPostingRepository.saveAndFlush(mapped);
        onJobPosted(Long.valueOf(employerId), savedJobPosting);
        return mapToDto(savedJobPosting);
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





    @Override
    public Flux<ServerSentEvent<List<JobPostingDto>>> sendJobPostingEvents() throws InterruptedException {
        return Flux.interval(Duration.ofSeconds(4))
                .publishOn(Schedulers.boundedElastic())
                .map(sequence -> ServerSentEvent.<List<JobPostingDto>>builder().id(String.valueOf(sequence))
                        .event("jobpostings")
                        .data(
                                Collections.singletonList(mapper.map(jobPostingRepository.findAll(), JobPostingDto.class)))
                        .build());
    }
}
