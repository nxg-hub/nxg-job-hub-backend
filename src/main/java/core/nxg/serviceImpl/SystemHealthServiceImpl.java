package core.nxg.serviceImpl;

import core.nxg.entity.User;
import core.nxg.repository.SessionRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.SystemHealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

@Service
public class SystemHealthServiceImpl implements SystemHealthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public long getTotalUsers() {
        return userRepository.count();
    }

//    @Override
//    public long getNewUsers() {
//        LocalDateTime oneMonthAgo = LocalDateTime.now(ZoneOffset.UTC).minus(1, ChronoUnit.MONTHS);
//        return userRepository.countNewUsersSince(oneMonthAgo);
//    }

    @Override
    public long getNewUsers1monthAgo() {
        try {
            LocalDateTime oneMonthAgo = LocalDateTime.now(ZoneOffset.UTC).minus(1, ChronoUnit.MONTHS);
            return userRepository.countNewUsersSince(oneMonthAgo);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            // Handle the exception as needed
            throw new RuntimeException("Error retrieving new users count: " + e.getMessage());
        }
    }

    @Override
    public long getNewUsers24HoursAgo() {
        try {
            LocalDateTime last24Hours = LocalDateTime.now(ZoneOffset.UTC).minus(1, ChronoUnit.DAYS);
            return userRepository.countNewUsersSince(last24Hours);
        } catch (Exception e) {
            // Log the error
            e.printStackTrace();
            // Handle the exception as needed
            throw new RuntimeException("Error retrieving new users count: " + e.getMessage());
        }
    }


    @Override
    public double getAverageTimeOnPlatform() {
        List<User> users = userRepository.findAllUsersForAverageTimeCalculation();
        OptionalDouble averageTime = users.stream().mapToDouble(User::getTimeOnPlatform).average();

        // Format the average to 2 decimal places
        return averageTime.isPresent() ? Math.round(averageTime.getAsDouble() * 100.0) / 100.0 : 0.0;
    }

    @Override
    public long getActiveNow() {
        return sessionRepository.countActiveSessions();
    }

    @Override
    public Map<String, Object> getSystemHealthMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalUsers", getTotalUsers());
        metrics.put("newUsers1monthAgo", getNewUsers1monthAgo());
        metrics.put("newUsers24HoursAgo", getNewUsers24HoursAgo());
        metrics.put("averageTimeOnPlatform", getAverageTimeOnPlatform() + "Seconds");
        metrics.put("activeNow", getActiveNow());
        return metrics;
    }
}
