package core.nxg.controller;

import core.nxg.service.AdminService;
import core.nxg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")

public class AdminController {

    private final AdminService adminService;

    private final UserService userService;

    @GetMapping("/{userType}")
    public ResponseEntity<Object> getUsersByType(
            @PathVariable String userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        userType = userType.toLowerCase();

        Object users;
        switch (userType) {
            case "techtalent" -> users = adminService.getTalentUsers(page, size).getContent();
            case "employer" -> users = adminService.getEmployerUsers(page, size).getContent();
            case "agent" -> users = adminService.getAgentUsers(page, size).getContent();
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Object> getAllTransactions(
            Pageable pageable) {
        var response =  adminService.getAllTransactions(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Object> getTransactionById(
            @PathVariable Long transactionId) {
        try {
            var response = adminService.getTransactionById(transactionId);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/jobs")
    public ResponseEntity<Object>getAllJobs(
            Pageable pageable) {
        var response = adminService.getAllJobs(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobs/{jobId}/accept")
    public ResponseEntity<Object> acceptJob(
            @PathVariable Long jobId) {
        adminService.acceptJob(jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/jobs/{jobId}/reject")
    public ResponseEntity<Object> rejectJob(
            @PathVariable Long jobId) {
        adminService.rejectJob(jobId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/jobs/{jobId}/suspend")
    public ResponseEntity<Object> suspendJob(
            @PathVariable Long jobId) {
       try {
           adminService.suspendJob(jobId);
           return ResponseEntity.ok().build();

       }catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
    }

    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<Object> suspendUser(
            @PathVariable Long userId) {
        try {
            adminService.suspendUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
