package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    Optional<JobPosting> findJobPostingByJobID(Long jobID);
    /* TODO: CHANGE ALL JOBID OCCURRENCES TO A LONG NOT STRING. */
}
