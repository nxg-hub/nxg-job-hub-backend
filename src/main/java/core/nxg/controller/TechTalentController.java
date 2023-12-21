package core.nxg.controller;

import core.nxg.dto.ApplicationDTO;
import core.nxg.dto.DashboardDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.TechTalentUser;
import core.nxg.repository.ApplicationRepository;
import core.nxg.repository.SavedJobRepository;
import core.nxg.service.ApplicationService;
import core.nxg.service.TechTalentService;
import core.nxg.service.UserService;
import core.nxg.utils.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createTechTalentUser (@Valid @RequestBody TechTalentDTO techTalentDTO,
                                                        HttpServletRequest request){
        try {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(techTalentService.createTechTalent( techTalentDTO,request));
        } catch (Exception e) {
            logger.error("Error creating TechTalentUser : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @Operation(description = "Get logged-in techtalent user",
            summary = "Get logged-in techtalent user using the bearer token")
    @GetMapping("/get-user")
    @ResponseBody
    public ResponseEntity<?> getTechTalent (HttpServletRequest request) throws Exception {
        TechTalentDTO techtalent = techTalentService.getTechTalent(request);
        return ResponseEntity.ok(techtalent);


    }

//    @PatchMapping("/{ID}")
//    @ResponseBody
//    public ResponseEntity<?> updateTechTalent (@PathVariable Long ID, @RequestBody Map<Object,Object> fields) throws Exception {
//        try {
//            TechTalentUser techtalent = techTalentService.updateTechTalent(String.valueOf(ID), fields);
//
//            return ResponseEntity.ok(techtalent);
//        }catch(Exception e){
//            log.error("Error updating:{}", e.getMessage());
//            return ResponseEntity.badRequest().body("You have made an invalid request!");
//        }
//    }


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

//    @Operation(description = "Get a logged-in techtalent's profile")
//    @GetMapping("/my-dashboard/profile")
//    @ResponseBody
//    public ResponseEntity<?> profile (HttpServletRequest request) throws Exception {
//        try {
//            UserResponseDto response = techTalentService.getMe(request);
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid!");
//        }
//    }

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

    // @GetMapping("/profile/{ID}")
    // public String getProfilePage(@PathVariable Long ID, HttpServletRequest request, Model model) {
    //     User user = helper.extractLoggedInUser(request);
    //     if (user.getId().equals(ID) ) {
    //         return "redirect:/api/v1/auth/tech-talent/my-dashboard";
    //     }
    //     else {
    //         return "error";
    //     }
    //     return "profile";


}


