package core.nxg.controller;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Application;
import core.nxg.entity.Employer;
import core.nxg.exceptions.NotFoundException;
import core.nxg.response.EmployerResponse;
import core.nxg.response.EngagementForEmployer;
import core.nxg.response.JobPostingResponse;
import core.nxg.service.EmployerService;
import core.nxg.serviceImpl.ApplicationServiceImpl;
import core.nxg.serviceImpl.EmployerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerServiceImpl employerService;
    private final ApplicationServiceImpl applicationService;

//    @GetMapping("/verify")
//    public ResponseEntity<String> verifyEmployer(HttpServletRequest request, @RequestParam("email") String email) {
//        String message = employerService.verifyEmployer(email, request);
//        return ResponseEntity.ok(message);
//    }
    @Operation(summary = "Create a new employer account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the employer",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployerDto.class)) }),
            @ApiResponse(responseCode = "400", description = "An invalid request was sent",
                    content = @Content),
            @ApiResponse(responseCode = "405", description = "Method not allowed",
                    content = @Content) })
    @PostMapping("/createEmployer")
    public ResponseEntity<String> createEmployer(@Valid @RequestBody  EmployerDto employerDTO, HttpServletRequest request) throws Exception {
        try {
            String message = employerService.createEmployer(employerDTO, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }catch(Exception e){
            log.error("Error while creating Employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Operation(summary = "Get an employer instance by bearer token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the employer",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployerResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "An Invalid request was sent",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @GetMapping("/get-employer")
    public ResponseEntity<EmployerResponse> getEmployerByJwt(HttpServletRequest request) throws Exception{

        try{
            EmployerResponse employer = employerService.getEmployer(request);
            return ResponseEntity.ok(employer);
        }catch(Exception e){
            log.error("Error while getting Employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    @Operation(summary = "Update an employer instance with the employer ID ")
    @io.swagger.v3.oas.annotations.parameters.RequestBody( description = "Fields to update", required = true,
            content =
            @Content(mediaType = "application/json",
            schema =
            @Schema(implementation = EmployerDto.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful update of employer instance",
                    content = {
                    @Content(mediaType = "application/json",
                            schema =
                            @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid request. Null values or empty fields not allowed",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employer not found",
                    content = @Content) })
    @RequestMapping("/{employerId}")
    public ResponseEntity<?> updateEmployer(@PathVariable String employerId, @RequestBody Map<Object, Object> fields)
            throws IllegalArgumentException, Exception {
        try {
            Employer updatedEmployer = employerService.patchEmployer(employerId, fields);
            return ResponseEntity.ok(updatedEmployer);
        } catch (Exception e) {
            log.error("Error while updating Employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{employerId}")
    public ResponseEntity<Void> deleteEmployer(@Valid @PathVariable String employerId) {
        employerService.deleteEmployer(employerId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get engagements for an employer by its id",
            description ="This endpoint returns engagements for an employer. Its a Pageable response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found engagements",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EngagementForEmployer.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied or an Invalid request was sent",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Employer not found",
                    content = @Content) })
    @GetMapping("engagements/{employerId}")
    public ResponseEntity<?> getEngagements(@PathVariable String employerId, Pageable pageable) throws Exception {
        try {
            return ResponseEntity.ok(employerService.getEngagements(employerId, pageable));
        } catch (Exception e) {
            log.error("Error while getting engagements: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get all job postings by an employer via its id,Its a Pageable response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found some job postings. Might be null. Check the response body for more details",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = JobPostingResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @GetMapping("postings/{employerId}")
    @ResponseBody
    public ResponseEntity<?> getJobPostings(@Valid @PathVariable String employerId) throws Exception {
        try {
            return ResponseEntity.ok(employerService.getJobPostings(employerId));
        } catch (NotFoundException | NullPointerException e) {
            log.error("Error while getting job postings: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Verify an existing employer by their ID",
            description ="This endpoint returns a boolean value indicating if the employer is verified or not.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employer is verified",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied/Employer not found",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Not found",
                    content = @Content) })
    @GetMapping("/verify/{employerId}")
    @ResponseBody
    public ResponseEntity<?> isEmployerVerified( @Valid @PathVariable String employerId) {
        try {
            return ResponseEntity.ok(employerService.isEmployerVerified(employerId));
        } catch (Exception e) {
            log.error("Error while verifying employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job/{jobId}/suggested-applicants")
    public ResponseEntity<?> getSuggestedApplicants(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "70") int scoreThreshold) {
        try {
            List<Application> applicants = applicationService.getSuggestedApplicants(jobId, scoreThreshold);
            return new ResponseEntity<>(applicants, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }






}


