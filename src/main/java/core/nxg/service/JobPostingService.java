package core.nxg.service;

import core.nxg.dto.JobPostingDto;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getAllJobPostings();

    JobPostingDto createJobPosting(JobPostingDto jobPostingDto);

    JobPostingDto getJobPostingById(String jobId);

    JobPostingDto updateJobPosting(String jobId, JobPostingDto jobPostingDto);

    void deleteJobPosting(String jobId);

//    void deleteJobPosting(JobPostingDto jobPostingDto);
}
