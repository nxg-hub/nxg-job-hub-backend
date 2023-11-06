package core.nxg.controller;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.service.EmployerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employers")
public class EmployerController {

    private final EmployerService employerService;


    @PostMapping("/create")
    public ResponseEntity<Employer> createEmployer(
            @RequestBody EmployerDto employerDto, HttpServletRequest request){
        Employer createdEmployer = employerService.createEmployer(employerDto, request);
        return new ResponseEntity<>(createdEmployer, HttpStatus.CREATED);
    }

    @GetMapping
    public List<EmployerDto> getAllEmployers() {
        return employerService.getAllEmployers();
    }

    @GetMapping("/{employerId}")
    public ResponseEntity<EmployerDto> getEmployerById(@PathVariable Long employerId) {
        EmployerDto employer = employerService.getEmployerById(employerId);
        return ResponseEntity.ok(employer);
    }

    @PutMapping("/{employerId}")
    public ResponseEntity<EmployerDto> updateEmployer(@PathVariable Long employerId, @RequestBody EmployerDto employerDto) {
        EmployerDto updatedEmployer = employerService.updateEmployer(employerId, employerDto);
        return ResponseEntity.ok(updatedEmployer);
    }

    @DeleteMapping("/{employerId}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable Long employerId) {
        employerService.deleteEmployer(employerId);
        return ResponseEntity.noContent().build();
    }

}
