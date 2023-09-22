package core.nxg.repository;

import core.nxg.entity.TechTalentUser;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface TechTalentRepository extends JpaRepository<TechTalentUser, Long> {
  TechTalentUser findByUsername(String email);
}
