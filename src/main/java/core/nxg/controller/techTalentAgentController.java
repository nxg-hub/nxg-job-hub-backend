package core.nxg.controller;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;
import core.nxg.serviceImpl.TechTalentAgentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agents")
public class techTalentAgentController {
    private final TechTalentAgentServiceImpl techTalentAgentService;

    @PostMapping("/create")
    public ResponseEntity<TechTalentAgent> createAgent(
            @RequestBody TechTalentAgentDto techTalentAgentDto){
        TechTalentAgent createdAgent = techTalentAgentService.createAgents(techTalentAgentDto);
        return new ResponseEntity<>(createdAgent, HttpStatus.CREATED);
    }

    @PutMapping("/{agentId}")
    public ResponseEntity<TechTalentAgentDto> updateTechTalentAgent(@PathVariable Long agentId, @RequestBody TechTalentAgentDto techTalentAgentDto) {
        TechTalentAgentDto updatedTechTalentAgent = techTalentAgentService.updateTechTalentAgent(agentId, techTalentAgentDto);
        return ResponseEntity.ok(updatedTechTalentAgent);
    }


    @GetMapping
    public List<TechTalentAgentDto> getAllTechTalentAgent() {
        return techTalentAgentService.getAllTechTalentAgent();
    }


    @GetMapping("/{agentId}")
    public ResponseEntity<TechTalentAgentDto> getTechTalentAgentById(@PathVariable Long agentId) {
        TechTalentAgentDto techTalentAgent = techTalentAgentService.getTechTalentAgentById(agentId);
        return ResponseEntity.ok(techTalentAgent);
    }

    @DeleteMapping("/{agentId}")
    public ResponseEntity<Void> deleteTechTalentAgent(@PathVariable Long agentId) {
        techTalentAgentService.deleteTechTalentAgent(agentId);
        return ResponseEntity.noContent().build();
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
}


