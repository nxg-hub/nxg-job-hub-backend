package core.nxg.controller;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.response.PaginatedResponse;
import core.nxg.service.TechTalentAgentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agents")
public class TechTalentAgentController {
    private final TechTalentAgentService techTalentAgentService;

    @GetMapping("/verify")
    public ResponseEntity<String> verifyTechTalentAgent(HttpServletRequest request, @RequestParam("email") String email) {
        String message = techTalentAgentService.verifyTechTalentAgent(email, request);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/createAgent")
    public ResponseEntity<String> createAgent(@RequestBody TechTalentAgentDto agentDTO) {
        String message = techTalentAgentService.createAgent(agentDTO);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }


    @PutMapping("/{agentId}")
    public ResponseEntity<TechTalentAgentDto> updateTechTalentAgent(@RequestBody TechTalentAgentDto techTalentAgentDto) {
        TechTalentAgentDto updatedTechTalentAgent = techTalentAgentService.updateTechTalentAgent(techTalentAgentDto);
        return ResponseEntity.ok(updatedTechTalentAgent);
    }


    @GetMapping
    public PaginatedResponse<TechTalentAgentDto> getAllTechTalentAgent(@RequestParam(value = "page", defaultValue = "1")int page,
                                                                       @RequestParam(value = "size", defaultValue = "10")int size) {
        return techTalentAgentService.getAllTechTalentAgent(page, size);
    }


    @GetMapping("/{Id}")
    public ResponseEntity<TechTalentAgentDto> getTechTalentAgentById(@PathVariable Long Id) {
        TechTalentAgentDto techTalentAgent = techTalentAgentService.getTechTalentAgentById(Id);
        return ResponseEntity.ok(techTalentAgent);
    }

    @DeleteMapping("/{agentId}")
    public ResponseEntity<Void> deleteTechTalentAgent(@PathVariable Long agentId) {
        techTalentAgentService.deleteTechTalentAgent(agentId);
        return ResponseEntity.noContent().build();
    }
}








































//    @PostMapping("/register/")
//    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) throws Exception{
//        try {
//            String response =  userService.createUser(userDTO);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (UserAlreadyExistException e) {
//
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//
//
//        }  catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Oops! Something went wrong. Please try again!");
//        }



//
//    @PostMapping("/create")
//    public ResponseEntity<TechTalentAgent> createAgent(
//            @RequestBody TechTalentAgentDto techTalentAgentDto) {
//
//        // Check if the user is registered
//        if (!isUserRegistered(techTalentAgentDto.getTechTalentAgent())) {
//            return new ResponseEntity<>("Tech Talent Agent is not a registered user.", HttpStatus.BAD_REQUEST);
//        }
//
//        // Check if jobType and industryType are valid enums
//        if (!isValidEnum(techTalentAgentDto.getJobType()) || !isValidEnum(techTalentAgentDto.getIndustryType())) {
//            return new ResponseEntity<>("Invalid jobType or industryType.", HttpStatus.BAD_REQUEST);
//        }
//
//        // Check if fields are not empty
//        if (isEmpty(techTalentAgentDto.getJobType()) || isEmpty(techTalentAgentDto.getIndustryType())) {
//            return new ResponseEntity<>("jobType and industryType cannot be empty.", HttpStatus.BAD_REQUEST);
//        }
//
//        TechTalentAgent createdAgent = agentService.createAgents(techTalentAgentDto);
//        return new ResponseEntity<>(createdAgent, HttpStatus.CREATED);
//    }
//
//    private boolean isUserRegistered(String techTalentAgent) {
//        // Implement logic to check if the techTalentAgent is a registered user
//        // Return true if registered, false otherwise
//        return true; // Replace this with your actual logic
//    }
//
//    private boolean isValidEnum(Enum<?> value) {
//        // Implement logic to check if the enum value is valid
//        // Return true if valid, false otherwise
//        return true; // Replace this with your actual logic
//    }
//
//    private boolean isEmpty(String value) {
//        // Implement logic to check if the string is empty or null
//        // Return true if empty, false otherwise
//        return value == null || value.trim().isEmpty();
//    }





