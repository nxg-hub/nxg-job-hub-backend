package core.nxg.controller;

import core.nxg.dto.JobPostingDto;
import core.nxg.service.JobPostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;
    // private final RatingsServiceImpl ratingsService;


    @PostMapping("/create")
    public ResponseEntity<JobPostingDto> createJobPosition(@RequestBody JobPostingDto jobPostingDto) {
        JobPostingDto jobPosting = jobPostingService.createJobPosting(jobPostingDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobPosting);
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobPostingDto>> getAllJobPosting() {
        List<JobPostingDto> jobPostings = jobPostingService.getAllJobPostings();
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/get-{jobID}")
    public ResponseEntity<JobPostingDto> getAJobPosting(@PathVariable Long jobID) {
        JobPostingDto jobPosting = jobPostingService.getJobPostingById(jobID);
        return ResponseEntity.ok(jobPosting);
    }

    @PutMapping("/update/{jobID}")
    public ResponseEntity<JobPostingDto> updateJobPosting(@PathVariable Long jobID, @RequestBody JobPostingDto jobPostingDto) {
        JobPostingDto updatedJobPost = jobPostingService.updateJobPosting(jobID, jobPostingDto);
        return ResponseEntity.ok(updatedJobPost);
    }

    @DeleteMapping("/delete/{jobID}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long jobID) {
        jobPostingService.deleteJobPosting(jobID);
        return ResponseEntity.noContent().build();
    }
}
