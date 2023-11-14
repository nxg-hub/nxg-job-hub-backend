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
    public ResponseEntity<JobPostingDto> createJobPosting(
            @RequestBody JobPostingDto jobPostingDto) {
        JobPostingDto createdJobPosting = jobPostingService.createJobPosting(jobPostingDto);
        return new ResponseEntity<>(createdJobPosting, HttpStatus.CREATED);
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long jobId) {
        jobPostingService.deleteJobPosting(jobId);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping("/delete/{jobId}")
//    public ResponseEntity<JobPostingDto> deleteJobPosting(@PathVariable Long jobId){
//      JobPostingDto deletedJobPosting = jobPostingService.getJobPostingById(jobId);
//      return ResponseEntity.ok(deletedJobPosting);
//    }


//   @DeleteMapping("/delete/{jobId}")
//    public ResponseEntity<String> deleteComment(@PathVariable Long jobId) {
//        String successMessage = jobPostingService.deleteJobPosting(jobId);
//        return ResponseEntity.ok(successMessage);
//    }


    @GetMapping("/all")
    public ResponseEntity<List<JobPostingDto>> getAllJobPosting() {
        List<JobPostingDto> jobPostings = jobPostingService.getAllJobPostings();
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/get-{jobId}")
    public ResponseEntity<JobPostingDto> getAJobPosting(@PathVariable Long jobId) {
        JobPostingDto jobPosting = jobPostingService.getJobPostingById(jobId);
        return ResponseEntity.ok(jobPosting);
    }

    @PutMapping("/update/{jobId}")
    public ResponseEntity<JobPostingDto> updateJobPosting(@PathVariable Long jobId, @RequestBody JobPostingDto jobPostingDto) {
        JobPostingDto updatedJobPost = jobPostingService.updateJobPosting(jobId, jobPostingDto);
        return ResponseEntity.ok(updatedJobPost);
    }

}
