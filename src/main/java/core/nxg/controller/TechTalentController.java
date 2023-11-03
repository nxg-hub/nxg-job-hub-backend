package core.nxg.controller;

import core.nxg.dto.JobPostingDto;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.ApplicationDTO;

import core.nxg.entity.Application;

import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.ApplicationService;
import core.nxg.service.TechTalentService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/tech-talent")
public class TechTalentController<T extends TechTalentDTO, S extends Pageable> {

    private final Logger logger = LoggerFactory.getLogger(TechTalentController.class);


    @Autowired
    private final TechTalentService<T> techTalentService;

    @Autowired
    Helper helper;
    
 
    @Autowired
    ApplicationService applicationService;
    
    public TechTalentController(TechTalentService<T> techTalentService) {
        this.techTalentService = techTalentService;
    }

    @PostMapping("/register/")
    public ResponseEntity<String> createTechTalentUser(@RequestBody TechTalentDTO techTalentDTO) {

        try {
            techTalentService.createTechTalent(techTalentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("TechTalent User created successfully");
        } catch (Exception e) {
            logger.error("Error creating TechTalentUser : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
           }  
    }




    @GetMapping("/users/")
    public ResponseEntity<Page<TechTalentDTO>> getAllTechTalentUsers(Pageable pageable) {
        try {
            Page<TechTalentDTO> users = techTalentService.getAllTechTalent(pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Tried fetching TechTalentUser(s) but : {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get-{ID}")
    public ResponseEntity<TechTalentDTO> getTechTalent(@PathVariable Long ID) throws Exception {
        TechTalentDTO techtalents = techTalentService.getTechTalentById(ID);
        return ResponseEntity.ok(techtalents);


};

    @PutMapping("/update-{ID}")
    public ResponseEntity<TechTalentDTO> updateTechTalent(@PathVariable Long ID, @RequestBody TechTalentDTO techTalentDTO) throws Exception {
        TechTalentDTO techtalents = techTalentService.updateTechTalent(techTalentDTO,ID);
        return ResponseEntity.ok(techtalents);
    }

    @GetMapping("/my-dashboard/job-applications")
    public Page<ApplicationDTO> getJobApplicationsForUser( HttpServletRequest request, Pageable pageable) throws Exception {
        User user = helper.extractLoggedInUser(request);
        return applicationService.getMyApplications(user, pageable);
    }
}

