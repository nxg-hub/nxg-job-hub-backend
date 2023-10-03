package core.nxg.controller;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.TechTalentRepository;
import core.nxg.service.TechTalentService;
import core.nxg.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/v1/tech-talent")
public class TechTalentController<T extends TechTalentDTO, S extends Pageable> {

    private final Logger logger = LoggerFactory.getLogger(TechTalentController.class);
    

    @Autowired
    private final TechTalentService<T> techTalentService;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private TechTalentRepository techTalentRepository;

    public TechTalentController(TechTalentService<T> techTalentService) {
        this.techTalentService = techTalentService;
    }

    @PostMapping("/register/")
    @PreAuthorize("hasAuthority('ROLE_TECHTALENT')")
    public ResponseEntity<String> createTechTalentUser(@RequestBody(required = false) T techTalentDTO) throws Exception {
        if (techTalentDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide the required fields");

        }
        Optional<User >Optional = userRepository.findByEmail(techTalentDTO.getEmail());
        Optional<TechTalentUser> techTalentUser = techTalentRepository.findById(Optional.get().getTechTalent().getTechId());
        if (techTalentUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }  
        try {
            TechTalentDTO user = techTalentService.createTechTalent(techTalentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("TechTalent User created successfully");
        } catch (UserNotFoundException e) {
 
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
                //e.printStackTrace();

            return ResponseEntity.badRequest().body(e.getMessage());
        
        }
    }
    


    @GetMapping("/users/")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Page<TechTalentUser>> getAllTechTalentUsers(T techTalentDTO, S pageable) {
        try {
            Page<TechTalentUser> users = techTalentService.getAllTechTalent(techTalentDTO, pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}