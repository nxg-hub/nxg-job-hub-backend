package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    //void deleteJobPostingByJobId(Long jobId);
    Optional<JobPosting> findJobPostingByJobID(Long jobID);
    Optional<JobPosting> findJobPostingByEmployerID(Long EmployerID);


    //Optional<Object> deleteJobPostingById(Long jobId);

    //Optional<Object> deleteJobPostingByJobId(Long jobId);
}
