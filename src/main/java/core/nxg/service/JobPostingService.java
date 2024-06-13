package core.nxg.service;

import core.nxg.dto.JobPostingDto;
import core.nxg.entity.JobPosting;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface JobPostingService {
    List<JobPostingDto> getAllJobPostings(Pageable pageable);

    JobPostingDto createJobPosting(JobPostingDto jobPostingDto) throws Exception;

    JobPostingDto getJobPostingById(Long jobId);

    JobPostingDto updateJobPosting(Long jobId, JobPostingDto jobPostingDto);

    void deleteJobPosting(Long jobId);


    Object recommendJobPosting(Long userId) throws Exception;

    public Flux<ServerSentEvent<CompletableFuture<List<JobPosting>>>> sendJobPostingEvents() throws InterruptedException ;

//    void deleteJobPosting(JobPostingDto jobPostingDto);
}
