package core.nxg.repository;

import core.nxg.entity.JobApplicationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface JobApplicationHistoryRepository extends MongoRepository<JobApplicationHistory, String> {
    List<JobApplicationHistory> findByJobId(String jobId);
    List<JobApplicationHistory> findByEmployerId(String employerId);
    List<JobApplicationHistory> findByAdminId(String adminId);
}
