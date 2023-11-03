package core.nxg.serviceImpl;

import core.nxg.dto.JobPostingDto;
import core.nxg.entity.JobPosting;
import core.nxg.exceptions.AlreadyExistException;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.JobPostingRepository;
import core.nxg.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobPostingServiceImpl implements JobPostingService {

    private final JobPostingRepository jobPostingRepository;

    @Override
    public List<JobPostingDto> getAllJobPostings() {
        List<JobPosting> jobPostings = jobPostingRepository.findAll();
        return jobPostings.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public JobPostingDto createJobPosting(JobPostingDto jobPostingDto) {
        Long jobID = jobPostingDto.getJobID();
        Optional<JobPosting> existingJobPosting = jobPostingRepository.findJobPostingByJobID(jobID);
        if (existingJobPosting.isPresent()) {
            throw new AlreadyExistException("Job posting with ID " + jobID + " already exists");
        }
        JobPosting jobPosting = new JobPosting();
        BeanUtils.copyProperties(jobPostingDto, jobPosting);
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return mapToDto(savedJobPosting);
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
//
//    @Override
//    public void deleteJobPosting(JobPostingDto jobPostingDto) {
//        // Convert JobPostingDto to JobPosting entity (assuming you have a conversion method or constructor)
//        JobPosting jobPosting = convertToJobPostingEntity(jobPostingDto);
//
//        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findJobPostingByJobID(jobPosting.getJobID());
//        if (optionalJobPosting.isPresent()) {
//            JobPosting existingJobPosting = optionalJobPosting.get();
//
//            // Check if the existing job post matches the provided details
//            if (existingJobPosting.equals(jobPosting)) {
//                jobPostingRepository.delete(existingJobPosting);
//            } else {
//                throw new NotFoundException("Job posting details provided do not match the existing record");
//            }
//        } else {
//            throw new NotFoundException("Job posting not found");
//        }
//    }
//
//    // Conversion method from JobPostingDto to JobPosting entity (example)
//    private JobPosting convertToJobPostingEntity(JobPostingDto jobPostingDto) {
//        JobPosting jobPosting = new JobPosting();
//        jobPosting.setJobID(jobPostingDto.getJobID());
//        // Set other properties accordingly
//        return jobPosting;
//    }


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

//    @Override
//    public void deleteJobPosting(JobPostingDto jobPostingDto) {
//
//    }

    private JobPostingDto mapToDto(JobPosting jobPosting) {
        JobPostingDto jobPostingDto = new JobPostingDto();
        BeanUtils.copyProperties(jobPosting, jobPostingDto);
        return jobPostingDto;
    }
}
