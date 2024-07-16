package core.nxg.controller;

import core.nxg.repository.SessionRepository;
import core.nxg.service.SystemHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/system-health")
public class SystemHealthController {

    @Autowired
    private SystemHealthService systemHealthService;

    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(systemHealthService.getTotalUsers());
    }

    @GetMapping("/new-users-1month-ago")
    public ResponseEntity<Long> getNewUsers1monthAgo() {
        return ResponseEntity.ok(systemHealthService.getNewUsers1monthAgo());
    }

    @GetMapping("/new-users-24hours-ago")
    public ResponseEntity<Long> getNewUsers24HoursAgo() {
        return ResponseEntity.ok(systemHealthService.getNewUsers24HoursAgo());
    }

    @GetMapping("/average-time-on-platform")
    public ResponseEntity<Double> getAverageTimeOnPlatform() {
        return ResponseEntity.ok(systemHealthService.getAverageTimeOnPlatform());
    }

    @GetMapping("/active-now")
    public ResponseEntity<Long> getActiveUsers() {
        long activeUsersCount = sessionRepository.countActiveSessions();
        return ResponseEntity.ok(activeUsersCount);
    }

    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getSystemHealthMetrics() {
        return ResponseEntity.ok(systemHealthService.getSystemHealthMetrics());
    }

    @GetMapping("/users/countNewUsersByMonthAndYear")
    public ResponseEntity<Long> getUserCountByMonth(@RequestParam int year, @RequestParam int month) {
        long userCount = systemHealthService.countUsersByMonth(year, month);
        return ResponseEntity.ok(userCount);
    }
}