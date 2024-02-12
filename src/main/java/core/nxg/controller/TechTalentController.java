package core.nxg.controller;

import core.nxg.dto.*;
import core.nxg.entity.TechTalentUser;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.repository.ApplicationRepository;
import core.nxg.repository.SavedJobRepository;
import core.nxg.response.TechTalentResponse;
import core.nxg.service.ApplicationService;
import core.nxg.service.TechTalentService;
import core.nxg.service.UserService;
import core.nxg.utils.Helper;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
@Slf4j
@RequestMapping("/api/v1/tech-talent")

public class TechTalentController<T extends TechTalentDTO, S extends Pageable> {

    private final Logger logger = LoggerFactory.getLogger(TechTalentController.class);


    @Autowired
    private TechTalentService<T> techTalentService;

    @Autowired
    Helper helper;


    @Autowired
    ApplicationService applicationService;

    @Autowired
    private SavedJobRepository savedJobRepo;

    @Autowired
    ApplicationRepository appRepo;

    @Autowired
    UserService userService;



    @Operation(summary = "Create a new TechTalentUser",
            description = "Create a techtalent account for logged-in user")

    @PostMapping("/register/")
    @ResponseBody
    public ResponseEntity<String> createTechTalentUser (@Valid @RequestBody TechTalentDTO techTalentDTO,
                                                        HttpServletRequest request) throws ExpiredJWTException, Exception{
        try {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(techTalentService.createTechTalent( techTalentDTO,request));
        } catch (Exception e) {
//            logger.error("Error creating TechTalentUser : {}", e.getMessage());
            logger.debug("Found error is", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @Operation(description = "Get logged-in techtalent user",
            summary = "Get logged-in techtalent user using the bearer token")

    @GetMapping("/get-user")
    @ResponseBody
    public ResponseEntity<TechTalentResponse> getTechTalent (HttpServletRequest request) throws Exception {
        try{
            TechTalentResponse employer = techTalentService.getTechTalent(request);
            return ResponseEntity.ok(employer);
        }catch(Exception e){
            log.error("Error while getting TechTalent: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Update a Tech Talent ",
            description = "This endpoint updates an existing Tech Talent")

    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tech Talent object that needs to be updated", required = true,
            content = @Content(schema = @Schema(implementation = TechTalentDTO.class)))

    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "Tech Talent updated successfully",
                    content = @Content
                            (schema = @Schema
                                    (implementation = TechTalentDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Tech Talent not found")
    })
    @RequestMapping("/{techID}")
    public ResponseEntity<?> updateTechTalent (@PathVariable String techID, @RequestBody Map<Object,Object> fields) throws Exception {
        try {
            TechTalentUser techtalent = techTalentService.updateTechTalent(techID, fields);

            return ResponseEntity.ok(techtalent);
        }catch(Exception e){
            log.error("Error updating:{}", e.getMessage());
            return ResponseEntity.badRequest().body("You have made an invalid request!");
        }
    }


    @Operation(description = "Get all applications for a logged-in techtalent",
            summary = "Get all applications for a logged-in techtalent via the bearer token")
    @GetMapping("/my-applications")
    @ResponseBody
    public ResponseEntity<?> getJobApplicationsForUser (HttpServletRequest request, Pageable pageable) throws
    Exception {
        try{
        return ResponseEntity.ok(applicationService.getMyApplications(request, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Oops! Something went wrong. Try again later.");
        }
    }

    @Operation(description = "Get all saved jobs for a logged-in techtalent",
            summary = "Get all saved jobs for a logged-in techtalent via the bearer token. A Pageable response.")

    @GetMapping("/my-jobs")
    @ResponseBody
    public ResponseEntity<?> getSavedJobs (HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable)
    {
        try {
            return ResponseEntity.ok(applicationService.getMySavedJobs(request, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid!");
        }

    }

    @Operation(description = "Get a logged-in techtalent's profile")
    @GetMapping("/my-dashboard/profile")
    @ResponseBody
    public ResponseEntity<?> profile (HttpServletRequest request) throws Exception {
        try {
            UserResponseDto response = techTalentService.getMe(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
        }
    }

    @Operation(description = "Get a logged-in techtalent's dashboard",
            summary = "Get a logged-in techtalent's dashboard via the bearer token. A Pageable response.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the dashboard",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DashboardDTO.class)) })})
    @GetMapping("/my-dashboard")
    @ResponseBody
    public ResponseEntity<?> getTechTalentDashboard (HttpServletRequest request, Pageable pageable) throws Exception
    {
        try {
            DashboardDTO dashboardDTO = techTalentService.getTechTalentDashboard(request, pageable);
            return ResponseEntity.ok(dashboardDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }


    }

    @Operation(description = "Update the TechTalent skills. Or add more skills",
            summary = "Get a logged-in techtalent's dashboard via the bearer token. A Pageable response.")

    @PostMapping("/add-skills")
    public ResponseEntity<String> updateSkills(@RequestBody List<String> skills, HttpServletRequest request){
        try{
            techTalentService.addNewSkills(request, skills);
            return ResponseEntity.ok().body("New skills added successfully");
        }catch(Exception e){
            return  ResponseEntity.badRequest().body("You have made an Invalid request!");
        }
    }


}


