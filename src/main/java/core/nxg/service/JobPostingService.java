package core.nxg.service;

import core.nxg.dto.JobPostingDto;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getAllJobPostings();

    JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception;

    JobPostingDto getJobPostingById(Long jobId);

    JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto);

    void deleteJobPosting(Long jobId);

//    void deleteJobPosting(JobPostingDto jobPostingDto);
}
