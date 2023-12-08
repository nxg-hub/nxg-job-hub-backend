package core.nxg.controller;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.service.EmployerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;

//    @GetMapping("/verify")
//    public ResponseEntity<String> verifyEmployer(HttpServletRequest request, @RequestParam("email") String email) {
//        String message = employerService.verifyEmployer(email, request);
//        return ResponseEntity.ok(message);
//    }

    @PostMapping("/createEmployer")
    public ResponseEntity<String> createEmployer(@Valid @RequestBody EmployerDto employerDTO, HttpServletRequest request) throws Exception {
        try {
            String message = employerService.createEmployer(employerDTO, request);
            return ResponseEntity.ok(message);
        }catch(Exception e){
            log.error("Error while creating Employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body("You have made an invalid request");
        }
    }

    @GetMapping("/get-employer")
    public ResponseEntity<EmployerDto> getEmployerById(HttpServletRequest request) throws Exception{
        try{
            EmployerDto employer = employerService.getEmployer(request);
            return ResponseEntity.ok(employer);
        }catch(Exception e){
            log.error("Error while getting Employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PatchMapping("/{Id}")
    public ResponseEntity<?> updateEmployer(@PathVariable String Id, @RequestBody Map<Object, Object> fields)
            throws Exception {
        try {
            Employer updatedEmployer = employerService.patchEmployer(Id, fields);
            return ResponseEntity.ok(updatedEmployer);
        } catch (Exception e) {
            log.error("Error while updating Employer: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteEmployer(@Valid @PathVariable Long Id) {
        employerService.deleteEmployer(Id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public List<EmployerDto> getAllEmployer() {
//
//        return employerService.getAllEmployer();
//    }


}


