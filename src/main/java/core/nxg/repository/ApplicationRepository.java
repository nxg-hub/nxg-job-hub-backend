package core.nxg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import core.nxg.entity.Application;
import java.util.List;
import java.util.Optional;

import core.nxg.entity.TechTalentUser;
import core.nxg.enums.ApplicationStatus;
import org.springframework.stereotype.Repository;


@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long>{
    Optional<List<Application>> findByApplicant(TechTalentUser applicant);
    Optional<List<Application>>findByStatus(ApplicationStatus status);
    
}
