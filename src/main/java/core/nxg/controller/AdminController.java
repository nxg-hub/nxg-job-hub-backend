package core.nxg.controller;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.entity.EmployerApprovalHistory;
import core.nxg.entity.TechTalentApprovalHistory;
import core.nxg.service.UserService;
import core.nxg.serviceImpl.AdminServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {


    @Autowired
    private final AdminServiceImpl adminService;

    @Autowired
    private final UserService userService;

    @GetMapping("/{userType}")
    public ResponseEntity<Object> getUsersByType(
            @PathVariable String userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request
    ) {
        userType = userType.toLowerCase();

        Object users;
        switch (userType) {
            case "techtalent" -> users = adminService.getTalentUsers(page, size, request).getContent();
            case "employer" -> users = adminService.getEmployerUsers(page, size, request).getContent();
            case "agent" -> users = adminService.getAgentUsers(page, size, request).getContent();
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/transactions")
    public ResponseEntity<Object> getAllTransactions(
            Pageable pageable,
            HttpServletRequest request) {
        var response = adminService.getAllTransactions(pageable, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions/{transactionId}")
    public ResponseEntity<Object> getTransactionById(
            @PathVariable String transactionId,
            HttpServletRequest request) {
        try {
            var response = adminService.getTransactionById(transactionId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/jobs")
    public ResponseEntity<Object> getAllJobs(
            Pageable pageable,
            HttpServletRequest request) {
        var response = adminService.getAllJobs(pageable, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/jobs/{jobId}/accept")
    public ResponseEntity<Map<String, String>> acceptJob(
            @PathVariable String jobId, HttpServletRequest request) {
        try {
            adminService.acceptJob(jobId, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Job accepted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/jobs/{jobId}/reject")
    public ResponseEntity<Map<String, String>> rejectJob(
            @PathVariable String jobId,
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        String disapprovalReason = requestBody.get("disapprovalReason");

        try {
            adminService.rejectJob(jobId, disapprovalReason, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Job rejected successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/jobs/{jobId}/suspend")
    public ResponseEntity<Object> suspendJob(
            @PathVariable String jobId,
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        String suspensionReason = requestBody.get("suspensionReason");
        try {
            adminService.suspendJob(jobId, suspensionReason, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Job suspended successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<Object> suspendUser(
            @PathVariable String userId,
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        String suspensionReason = requestBody.get("reasonForProfileSuspension");
        try {
            adminService.suspendUser(userId, suspensionReason, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User suspended successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@NonNull @RequestBody UserDTO dto, HttpServletRequest request) {

        try {


            Object response = adminService.createAdmin(dto, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }


    }


    @PostMapping("/login")
    @CrossOrigin(exposedHeaders = "Authorization")
    public ResponseEntity<?> login(@NonNull @RequestBody LoginDTO dto, HttpServletRequest request) {
        try {
            String token = adminService.login(dto, request);
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<?> getAllSubscriptions(@PageableDefault(size = 5) Pageable pageable,
                                                 HttpServletRequest request) {
        try {
            var response = adminService.getSubscriptions(pageable, request);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

    @GetMapping("/techtalent/{techID}/verify")

        public ResponseEntity<Map<String, String>> verifyTechTalent(@PathVariable String techID) {
            Map<String, String> response = new HashMap<>();
            try {
                adminService.verifyTechTalent(techID);
                response.put("message", "Tech Talent Verified Successfully");
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("error", "Failed to verify Tech Talent: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
    }

    @GetMapping("/employer/{employerID}/verify")
    public ResponseEntity<Map<String, String>> verifyEmployer(@PathVariable String employerID) {
        Map<String, String> response = new HashMap<>();
        try {
            adminService.verifyEmployer(employerID);
            response.put("message", "Employer Verified Successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Failed to verify Employer: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/approval-history/{userType}")
    public ResponseEntity<Object> getUserApprovalHistoryByType(
            @PathVariable String userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            HttpServletRequest request
    ) {
        userType = userType.toLowerCase();

        Object approvalHistory;
        switch (userType) {
            case "techtalent":
                Page<TechTalentApprovalHistory> techTalentApprovalHistoryPage = adminService.getTechTalentApprovalHistory(page, size,request);
                approvalHistory = techTalentApprovalHistoryPage.getContent();
                break;
            case "employer":
                Page<EmployerApprovalHistory> employerApprovalHistoryPage = adminService.getEmployerApprovalHistory(page, size, request);
                approvalHistory = employerApprovalHistoryPage.getContent();
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(approvalHistory);
    }

    @PostMapping("/{employerId}/reject-verification")
    public ResponseEntity<Object> rejectEmployerVerification(
            @PathVariable String employerId,
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        String reasonForRejection = requestBody.get("reasonForRejection");
        try {
            adminService.rejectEmployerVerification(employerId, reasonForRejection, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Employer verification rejected successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/{techId}/reject-verification")
    public ResponseEntity<Object> rejectTechTalentVerification(
            @PathVariable String techId,
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        String reasonForRejection = requestBody.get("reasonForRejection");
        try {
            adminService.rejectTechTalentVerification(techId, reasonForRejection, request);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Tech Talent verification rejected successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}