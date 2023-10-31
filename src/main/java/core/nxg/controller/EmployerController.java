package core.nxg.controller;

import core.nxg.dto.EmployerDto;
import core.nxg.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
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
    public ResponseEntity<String> createEmployer(@RequestParam("email") String email, @RequestBody EmployerDto employerDTO) {
        String message = employerService.createEmployer(email, employerDTO);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<EmployerDto> getEmployerById(@PathVariable Long Id) {
        EmployerDto employer = employerService.getEmployerById(Id);
        return ResponseEntity.ok(employer);
    }

    @PutMapping("/{Id}")
    public ResponseEntity<String> updateEmployer(@PathVariable Long Id, @RequestBody EmployerDto employerDto) {
        String updatedEmployer = employerService.updateEmployer(Id, employerDto);
        return ResponseEntity.ok(updatedEmployer);
    }

    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable Long Id) {
        employerService.deleteEmployer(Id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<EmployerDto> getAllEmployer() {

        return employerService.getAllEmployer();
    }


}


