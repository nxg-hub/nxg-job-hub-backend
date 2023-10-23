package core.nxg.repository;

import core.nxg.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    Optional<JobPosting> findJobPostingByJobID(String jobID);
    /* TODO: CHANGE ALL JOBID OCCURRENCES TO A LONG NOT STRING. */
}
