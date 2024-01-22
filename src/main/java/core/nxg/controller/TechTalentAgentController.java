package core.nxg.controller;

import core.nxg.dto.TechTalentAgentDto;
import core.nxg.entity.TechTalentAgent;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.service.TechTalentAgentService;
import core.nxg.serviceImpl.TechTalentAgentServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agents")
public class TechTalentAgentController {

    @Autowired
    private final TechTalentAgentServiceImpl techTalentAgentService;


    @Operation(summary = "Create a new Tech Talent Agent",
    description = "This endpoint creates a new Tech Talent Agent")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tech Talent Agent object that needs to be created",
    required = true,content = @Content(schema = @Schema(implementation = TechTalentAgentDto.class)))

    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "Tech Talent Agent created successfully",
                    content = @Content(schema = @Schema(implementation = String.class)) ),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/createAgent")
    public ResponseEntity<String> createAgent(@RequestBody TechTalentAgentDto agentDTO, HttpServletRequest request) throws ExpiredJWTException {
        String message = techTalentAgentService.createAgent(agentDTO, request);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }



    @Operation(summary = "Update a Tech Talent Agent",
    description = "This endpoint updates an existing Tech Talent Agent")

    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Tech Talent Agent object that needs to be updated", required = true,
    content = @Content(schema = @Schema(implementation = TechTalentAgentDto.class)))

    @ApiResponses(value= {
            @ApiResponse(responseCode = "200", description = "Tech Talent Agent updated successfully",
                    content = @Content
                            (schema = @Schema
                                    (implementation = TechTalentAgent.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Tech Talent Agent not found")
    })
    @RequestMapping("/{agentId}")
    public ResponseEntity<?> updateTechTalentAgent(@RequestParam String agentID, @RequestBody Map<Object,Object> fields) {
        TechTalentAgent updatedTechTalentAgent = techTalentAgentService.patchAgent(agentID, fields);
        return ResponseEntity.ok(updatedTechTalentAgent);
    }




    @Operation(summary = "Get a Tech Talent Agent by Id",
    description = "This endpoint gets a Tech Talent Agent by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tech Talent Agent found",
                    content = @Content(schema = @Schema(implementation = TechTalentAgentDto.class))),
            @ApiResponse(responseCode = "404", description = "Tech Talent Agent not found")
    })
    @GetMapping("/{Id}")
    public ResponseEntity<TechTalentAgentDto> getTechTalentAgentById(@PathVariable Long Id) {
        TechTalentAgentDto techTalentAgent = techTalentAgentService.getTechTalentAgentById(Id);
        return ResponseEntity.ok(techTalentAgent);
    }

    @Operation(summary = "Delete a Tech Talent Agent")
    @DeleteMapping("/{agentId}")
    public ResponseEntity<Void> deleteTechTalentAgent(@PathVariable Long agentId) {
        techTalentAgentService.deleteTechTalentAgent(agentId);
        return ResponseEntity.noContent().build();
    }

    //    @GetMapping
//    public PaginatedResponse<TechTalentAgentDto> getAllTechTalentAgent(@RequestParam(value = "page", defaultValue = "1")int page,
//                                                                       @RequestParam(value = "size", defaultValue = "10")int size) {
//        return techTalentAgentService.getAllTechTalentAgent(page, size);
//    }

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





