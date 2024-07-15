package core.nxg.service;

import java.util.Map;

public interface SystemHealthService {
    long getTotalUsers();
    long getNewUsers1monthAgo();

    long getNewUsers24HoursAgo();

    double getAverageTimeOnPlatform();
    long getActiveNow();
    Map<String, Object> getSystemHealthMetrics();
}
