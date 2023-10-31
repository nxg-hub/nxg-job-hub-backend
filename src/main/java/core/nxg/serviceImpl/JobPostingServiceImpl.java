package core.nxg.serviceImpl;

import core.nxg.dto.JobPostingDto;
import core.nxg.entity.JobPosting;
import core.nxg.entity.TechTalentAgent;
import core.nxg.exceptions.AlreadyExistException;
import core.nxg.exceptions.ErrorException;
import core.nxg.exceptions.NotFoundException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.JobPostingRepository;
import core.nxg.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

        Long jobId = jobPostingDto.getJobId();
        Optional<JobPosting> existingJobPosting = jobPostingRepository.findJobPostingByJobId(jobId);
        if (existingJobPosting.isPresent()) {
            throw new AlreadyExistException("Job posting with Id " + jobId + " already exists");
        }

        JobPosting jobPosting = new JobPosting();
        BeanUtils.copyProperties(jobPostingDto, jobPosting);
        JobPosting savedJobPosting = jobPostingRepository.save(jobPosting);
        return mapToDto(savedJobPosting);
    }

    @Override
    public JobPostingDto getJobPostingById(Long jobId) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findJobPostingByJobId(jobId);
        if (optionalJobPosting.isPresent()) {
            return mapToDto(optionalJobPosting.get());
        } else {
            throw new NotFoundException("Job posting with Id " + jobId + " not found");
        }
    }


    @Override
    public JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findJobPostingByJobId(jobId);
        if (optionalJobPosting.isPresent()) {
            JobPosting existingJobPosting = optionalJobPosting.get();

            // Validate if the provided job ID in the path matches the job ID in the request body
            if (!jobId.equals(jobPostingDto.getJobId())) {
                throw new NotFoundException("Job ID in the path does not match the request body");
            }

            // Update fields that are allowed to be modified
            existingJobPosting.setTitle(jobPostingDto.getTitle());
            existingJobPosting.setDescription(jobPostingDto.getDescription());
            existingJobPosting.setSalary(jobPostingDto.getSalary());
            existingJobPosting.setJobType(jobPostingDto.getJobType());
            existingJobPosting.setDeadline(jobPostingDto.getDeadline());
            existingJobPosting.setLocation(jobPostingDto.getLocation());
            existingJobPosting.setTags(jobPostingDto.getTags());


            JobPosting updatedJobPosting = jobPostingRepository.save(existingJobPosting);

            return mapToDto(updatedJobPosting);
        } else {
            throw new NotFoundException("Job posting with Id " + jobId + " not found");
        }
    }

    // Utility method to map JobPosting entity to JobPostingDto
    private JobPostingDto mapToDto(JobPosting jobPosting) {
        JobPostingDto jobPostingDto = new JobPostingDto();
        jobPostingDto.setJobId(jobPosting.getJobId());
        jobPostingDto.setTitle(jobPosting.getTitle());
        jobPostingDto.setDescription(jobPosting.getDescription());
        jobPostingDto.setSalary(jobPosting.getSalary());
        jobPostingDto.setJobType(jobPosting.getJobType());
        jobPostingDto.setDeadline(jobPosting.getDeadline());
        jobPostingDto.setLocation(jobPosting.getLocation());
        jobPostingDto.setTags(jobPosting.getTags());
        jobPostingDto.setComments(jobPosting.getComments().toString());
        jobPostingDto.setReactions(jobPosting.getReaction());

        // Set other properties of jobPostingDto using getters or additional mapping logic
        return jobPostingDto;
    }


//    @Transactional
//    public void deleteJobPosting(Long jobId) {
//        try {
//            jobPostingRepository.deleteById(jobId);
//        } catch (UserNotFoundException e) {
//            throw new NotFoundException("Job Posting with jobId " + jobId + " not found");
//        } catch (Exception e) {
//            throw new ErrorException("Failed to delete job posting with jobId: " + jobId, e);
//        }
//    }

    @Override
    public void deleteJobPosting(Long jobId) {
        JobPosting jobPosting = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NotFoundException("TechTalentAgent with ID " + jobId + " not found"));
        jobPostingRepository.delete(jobPosting);
    }

}


