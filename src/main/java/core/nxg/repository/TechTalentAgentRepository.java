package core.nxg.repository;

import core.nxg.entity.TechTalentAgent;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechTalentAgentRepository extends JpaRepository<TechTalentAgent, Long> {
    Optional<TechTalentAgent> findByJobTypeAndIndustryType(JobType jobType, IndustryType industryType);
    Optional<TechTalentAgent> findById(Long id);

    Optional<TechTalentAgent> findByUserEmail(String email);


    boolean existsByEmail(String email);

    Optional<TechTalentAgent> findByEmail(String email);
}
