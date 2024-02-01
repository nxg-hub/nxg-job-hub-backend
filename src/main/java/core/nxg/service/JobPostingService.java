package core.nxg.service;

import core.nxg.dto.JobPostingDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;

public interface JobPostingService {
    List<JobPostingDto> getAllJobPostings(Pageable pageable);

    JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception;

    JobPostingDto getJobPostingById(Long jobId);

    JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto);

    void deleteJobPosting(Long jobId);

    public Flux<ServerSentEvent<List<JobPostingDto>>> sendJobPostingEvents() throws InterruptedException ;

//    void deleteJobPosting(JobPostingDto jobPostingDto);
}
