package core.nxg.controller;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.TechTalentRepository;
import core.nxg.service.TechTalentService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> createTechTalentUser(@RequestBody TechTalentDTO techTalentDTO) {

        // Check if the user with the provided email exists
        Optional<User> existingUser = userRepository.findByEmail(techTalentDTO.getEmail());
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User does not exist");
        }

        // Check if a TechTalentUser already exists for this User
        Optional<TechTalentUser> existingTechTalentUser = techTalentRepository.findByUser(existingUser.get());
        if (existingTechTalentUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("TechTalent User already exists");
        } 
        else{

            try {
                // Create the TechTalentUser and return a success response
                techTalentService.createTechTalent(techTalentDTO);
                return ResponseEntity.status(HttpStatus.CREATED).body("TechTalent User created successfully");
            } catch (UserNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            } catch (Exception e) {
                logger.error("Error creating TechTalentUser: {}", e.getMessage());
                return ResponseEntity.badRequest().body("An error occurred while creating the TechTalent User");
            }
    }
}

    


    @GetMapping("/users/")
    public ResponseEntity<Page<TechTalentDTO>> getAllTechTalentUsers(Pageable pageable) {
        try {
            Page<TechTalentDTO> users = techTalentService.getAllTechTalent(pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            System.out.print(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}