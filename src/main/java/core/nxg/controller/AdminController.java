package core.nxg.controller;

import core.nxg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")

public class AdminController {

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
            case "techtalent" -> users = userService.getTalentUsers(page, size).getContent();
            case "employer" -> users = userService.getEmployerUsers(page, size).getContent();
            case "agent" -> users = userService.getAgentUsers(page, size).getContent();
            default -> {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok(users);
    }
}
