package core.nxg.repository;

import core.nxg.entity.JobPosting;
import core.nxg.response.JobPostingResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {

    //void deleteJobPostingByJobId(Long jobId);
    Optional<JobPosting> findJobPostingByJobID(Long jobID);

    @Modifying
    @Query("SELECT j FROM JobPosting j WHERE j.employerID = ?1")
   Optional<List<JobPosting>> findByEmployerID(String employerID);


}
