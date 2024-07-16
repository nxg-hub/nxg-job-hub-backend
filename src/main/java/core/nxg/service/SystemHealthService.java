package core.nxg.service;

import core.nxg.entity.User;

import java.util.List;
import java.util.Map;

public interface SystemHealthService {
    long getTotalUsers();
    long getNewUsers1monthAgo();

    long getNewUsers24HoursAgo();

    double getAverageTimeOnPlatform();
    long getActiveNow();
    Map<String, Object> getSystemHealthMetrics();

    long countUsersByMonth(int year, int month);
}
