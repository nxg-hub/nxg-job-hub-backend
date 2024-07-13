package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public interface JobPostingRepository extends MongoRepository<JobPosting, String> {

    //void deleteJobPostingByJobId(Long jobId);
    Optional<JobPosting> findJobPostingByJobID(String jobID);// Added

    List<JobPosting> findByDeliveredFalse();
   Optional<List<JobPosting>> findByEmployerID(String employerID);

    Page<JobPosting> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);


}
