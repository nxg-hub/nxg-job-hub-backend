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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;
    private final EmployerRepository employerRepository;
    private final EmailService emailService;
    private final UserService userService;

    @Override
    public List<JobPostingDto> getAllJobPostings(Pageable pageable){
        List<JobPosting> jobPostings = jobPostingRepository.findAll();
        return jobPostings.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception{


        JobPosting jobPosting = new JobPosting();
        Long empoyerId = jobPostingDto.getEmployerID();
        BeanUtils.copyProperties(jobPostingDto, jobPosting);
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        onJobPosted(empoyerId, savedJobPosting);
        return mapToDto(savedJobPosting);
    }
    private void onJobPosted(Long employerId, JobPosting jobPosting)throws Exception{
        //TODO send email to only subscribed tech talent agents and tech talent
        Employer poster = employerRepository.findById(employerId).
                orElseThrow(() -> new NotFoundException("Employer was not found!"));

        Page<UserResponseDto> users = userService.getAllUsers(Pageable.unpaged());

        users.forEach(user -> {
            final String posterName = poster.getCompanyName();
            try {
                log.info("Preparing to send an email to {}", user.getEmail());

                emailService.sendJobPostingNotifEmail(user.getEmail(), jobPosting);

                log.atTrace().log("Email sent to {}", user.getEmail());
            } catch (Exception e) {
                throw new RuntimeException("Error sending email to {}" + user.getEmail() );
        }});




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
}
