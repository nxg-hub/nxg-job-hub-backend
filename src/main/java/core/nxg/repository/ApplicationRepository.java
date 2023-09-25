package core.nxg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import core.nxg.entity.Application;
import java.util.List;
import core.nxg.entity.TechTalentUser;
import core.nxg.enums.ApplicationStatus;



public interface ApplicationRepository extends JpaRepository<Application, Long>{
    List<Application> findByApplicant(TechTalentUser applicant);
    List<Application> findByStatus(ApplicationStatus status);
    
}
