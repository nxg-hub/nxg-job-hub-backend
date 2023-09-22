package core.nxg.repository;

import core.nxg.entity.TechTalentAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechTalentAgentRepository extends JpaRepository<TechTalentAgent, Long> {

}
