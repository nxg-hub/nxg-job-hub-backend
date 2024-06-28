package core.nxg.repository;

import com.mongodb.lang.NonNull;
import core.nxg.entity.TechTalentAgent;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TechTalentAgentRepository extends MongoRepository<TechTalentAgent, String> {
    Optional<TechTalentAgent> findByJobTypeAndIndustryType(JobType jobType, IndustryType industryType);


    Optional<TechTalentAgent> findById(@NonNull Long id);

    Optional<TechTalentAgent> findByUserEmail(String email);


    boolean existsByEmail(String email);

    Optional<TechTalentAgent> findByEmail(String email);
}
