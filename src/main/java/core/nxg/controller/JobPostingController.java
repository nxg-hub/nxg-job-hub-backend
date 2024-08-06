package core.nxg.controller;

import core.nxg.dto.ApplicationDTO;
import core.nxg.dto.JobPostingDto;
import core.nxg.entity.JobPosting;
import core.nxg.service.JobPostingService;
import core.nxg.serviceImpl.ApplicationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.data.domain.Page;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
public class JobPostingController {


    private final JobPostingService jobPostingService;
    // private final RatingsServiceImpl ratingsService;

    private final ApplicationServiceImpl applicationService;

    @Operation(summary = "Make a job posting as an employer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a job posting",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingDto.class))}),
            @ApiResponse(responseCode = "400", description = "An unrecognised or invalid request was sent",
                    content = @Content)})

    @PostMapping("/employer-post-job")
    public ResponseEntity<?> createJobPosition(@Valid @NonNull @RequestBody JobPostingDto jobPostingDto) {
        try {
            jobPostingService.createJobPosting(jobPostingDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Job Posted Successfully");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @Operation(summary = "All job postings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found job postings",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content)})
    @GetMapping("/all")
    public ResponseEntity<List<JobPostingDto>> getAllJobPosting(@PageableDefault(size = 10) Pageable page) {
        List<JobPostingDto> jobPostings = jobPostingService.getAllJobPostings(page);
        return ResponseEntity.ok(jobPostings);
    }


    @Operation(summary = "Get a job by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the job",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingDto.class)),}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied")})
    @GetMapping("/get-{jobID}")
    public ResponseEntity<JobPostingDto> getAJobPosting(@PathVariable String jobID) {
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

    @GetMapping("/{userId}/recommend")
    public ResponseEntity<?> recommendJobPosting(@PathVariable String userId) throws Exception {
        try {
            Object recommendedJobs = jobPostingService.recommendJobPosting(userId);
            return ResponseEntity.ok(recommendedJobs);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/recommend-nearby-jobs")
    public ResponseEntity<List<JobPosting>> getNearbyJobPostings(@RequestParam String userCity) {
        List<JobPosting> nearbyJobPostings = jobPostingService.getNearbyJobPostings(userCity);

//        // Convert entities to DTOs
//        List<JobPosting> nearbyJobPostingDtos = nearbyJobPostings.stream().map(this::convertToDto).collect(Collectors.toList());

        return ResponseEntity.ok(nearbyJobPostings);
    }

//    private JobPostingDto convertToDto(JobPosting jobPosting) {
//        JobPostingDto dto = new JobPostingDto();
//        dto.setEmployerID(jobPosting.getEmployerID());
//        dto.setJob_title(jobPosting.getJob_title());
//        dto.setJob_description(jobPosting.getJob_description());
//        dto.setCompany_bio(jobPosting.getCompany_bio());
//        dto.setSalary(jobPosting.getSalary());
//        dto.setJob_type(jobPosting.getJob_type());
//        dto.setDeadline(jobPosting.getDeadline());
//        dto.setCreatedAt(jobPosting.getCreatedAt());
//        dto.setRequirements(jobPosting.getRequirements());
//        dto.setJob_location(jobPosting.getJob_location());
//        dto.setTags(jobPosting.getTags());
//        return dto;
//    }

    @PostMapping("/{jobID}/apply")
    public ResponseEntity<?> apply(@Valid HttpServletRequest request, @RequestBody ApplicationDTO dto) throws Exception {

        try {


            applicationService.createApplication(request, dto);
            return ResponseEntity.ok("Application Successful!");

        } catch (Exception ex) {

            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/{jobID}/save")
    public ResponseEntity<?> saveJob(HttpServletRequest request, @PathVariable String jobID) throws Exception {
        try {

            applicationService.saveJob(request, jobID);
            return ResponseEntity.ok().build();

        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/recent-job-postings")
    public ResponseEntity<Object> getRecentJobPostings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<JobPosting> jobPostings = jobPostingService.getRecentJobPostings(page, size);
        return ResponseEntity.ok(jobPostings.getContent());
    }
}


