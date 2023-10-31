package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    //void deleteJobPostingByJobId(Long jobId);
    Optional<JobPosting> findJobPostingByJobId(Long jobId);


    //Optional<Object> deleteJobPostingById(Long jobId);

    //Optional<Object> deleteJobPostingByJobId(Long jobId);
}
