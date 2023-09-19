package core.nxg.repository;

import core.nxg.entity.TechTalent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechTalentRepository extends JpaRepository<TechTalent, Long> {
    List<TechTalent> findByEmail(String email);
}
