package core.nxg.service;

import core.nxg.dto.JobPostingDto;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getAllJobPostings();

    JobPostingDto createJobPosting(JobPostingDto jobPostingDto);

    JobPostingDto getJobPostingById(Long jobId);


    //void deleteJobPosting(Long jobId);

    void deleteJobPosting(Long jobId);

    JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto);

}
