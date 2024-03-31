package core.nxg.controller;

import core.nxg.dto.JobPostingDto;
import core.nxg.entity.JobPosting;
import core.nxg.service.JobPostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.awt.print.Book;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
public class JobPostingController {

    private final JobPostingService jobPostingService;
    // private final RatingsServiceImpl ratingsService;

    @Operation(summary = "Make a job posting as an employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a job posting",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingDto.class)) }),
            @ApiResponse(responseCode = "400", description = "An unrecognised or invalid request was sent",
                    content = @Content)})
    @PostMapping("/post")
    public ResponseEntity<?> createJobPosition(@Valid @NonNull @RequestBody JobPostingDto jobPostingDto) throws Exception{
        try{
            JobPostingDto jobPosting = jobPostingService.createJobPosting(jobPostingDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(jobPosting);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @Operation(summary = "All job postings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found job postings",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content)})
    @GetMapping("/all")
    public ResponseEntity<List<JobPostingDto>> getAllJobPosting(@PageableDefault(size = 10) Pageable page){
        List<JobPostingDto> jobPostings = jobPostingService.getAllJobPostings(page);
        return ResponseEntity.ok(jobPostings);
    }


    @Operation(summary = "Get a job by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the job",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingDto.class)), }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied")})
    @GetMapping("/get-{jobID}")
    public ResponseEntity<JobPostingDto> getAJobPosting(@PathVariable Long jobID) {
        JobPostingDto jobPosting = jobPostingService.getJobPostingById(jobID);
        return ResponseEntity.ok(jobPosting);
    }


    @Operation(summary = "Update a job by its id")
    @PutMapping("/update/{jobID}")
    public ResponseEntity<JobPostingDto> updateJobPosting(@PathVariable Long jobID, @RequestBody JobPostingDto jobPostingDto) {
        JobPostingDto updatedJobPost = jobPostingService.updateJobPosting(jobID, jobPostingDto);
        return ResponseEntity.ok(updatedJobPost);
    }

    @Operation(summary = "Delete a job by its id")
    @DeleteMapping("/delete/{jobID}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long jobID) {
        jobPostingService.deleteJobPosting(jobID);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/stream")
    public Flux<ServerSentEvent<CompletableFuture<List<JobPosting>>>> streamJobPostings() throws InterruptedException {
        return jobPostingService.sendJobPostingEvents();
    }
}


