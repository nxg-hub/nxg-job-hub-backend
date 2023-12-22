package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    //void deleteJobPostingByJobId(Long jobId);
    Optional<JobPosting> findJobPostingByJobID(Long jobID);
    Optional<List<JobPosting>> findByEmployerID(Long EmployerID, Pageable pageable);


    //Optional<Object> deleteJobPostingById(Long jobId); Pagea

    //Optional<Object> deleteJobPostingByJobId(Long jobId);
}
