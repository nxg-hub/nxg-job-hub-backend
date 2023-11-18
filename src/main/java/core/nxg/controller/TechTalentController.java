package core.nxg.controller;

import core.nxg.dto.ApplicationDTO;
import core.nxg.dto.DashboardDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.repository.ApplicationRepository;
import core.nxg.repository.SavedJobRepository;
import core.nxg.service.ApplicationService;
import core.nxg.service.TechTalentService;
import core.nxg.service.UserService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
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


@Controller
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
    SavedJobRepository savedJobRepo;

    @Autowired
    ApplicationRepository appRepo;

    @Autowired
    UserService userService;



    @PostMapping("/register/")
    @ResponseBody
    public ResponseEntity<String> createTechTalentUser (@RequestBody TechTalentDTO techTalentDTO, HttpServletRequest
    request){

        try {
            techTalentService.createTechTalent(request, techTalentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("TechTalent User created successfully");
        } catch (Exception e) {
            logger.error("Error creating TechTalentUser : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


//    @GetMapping("/users/")
//    @ResponseBody
//    public ResponseEntity<Page<?>> getAllTechTalentUsers (Pageable pageable){
//        try {
//            Page<TechTalentDTO> users = techTalentService.getAllTechTalent(pageable);
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        } catch (Exception e) {
//            logger.error("Tried fetching TechTalentUser(s) but : {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping("/get-{ID}")
    @ResponseBody
    public ResponseEntity<?> getTechTalent (@PathVariable Long ID) throws Exception {
        TechTalentDTO techtalent = techTalentService.getTechTalentById(ID);
        return ResponseEntity.ok(techtalent);


    }

    @PutMapping("/update-{ID}")
    @ResponseBody
    public ResponseEntity<TechTalentDTO> updateTechTalent (@PathVariable Long ID, @RequestBody TechTalentDTO
    techTalentDTO) throws Exception {
        TechTalentDTO techtalent = techTalentService.updateTechTalent(techTalentDTO, ID);
        return ResponseEntity.ok(techtalent);
    }

    @GetMapping("/my-dashboard/my-applications")
    @ResponseBody
    public ResponseEntity<?> getJobApplicationsForUser (HttpServletRequest request, Pageable pageable) throws
    Exception {
        return ResponseEntity.ok(applicationService.getMyApplications(request, pageable));
    }

    @GetMapping("/my-dashboard/saved")
    @ResponseBody
    public ResponseEntity<?> getSavedJobs (HttpServletRequest request, @PageableDefault(size = 10) Pageable pageable)
    {
        try {
            return ResponseEntity.ok(applicationService.getMySavedJobs(request, pageable));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid!");
        }

    }

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


