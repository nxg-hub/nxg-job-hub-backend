package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    //void deleteJobPostingByJobId(Long jobId);
//    Optional<JobPosting> findJobPostingByJobID(Long jobID);

    List<JobPosting> findByDeliveredFalse();
   Optional<List<JobPosting>> findByEmployerID(String employerID);


}
