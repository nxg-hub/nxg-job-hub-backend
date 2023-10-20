package core.nxg.repository;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.stereotype.Repository;
import core.nxg.entity.TechTalentUser;


@Repository
@Primary
public interface TechTalentRepository extends JpaRepository<TechTalentUser, Long> {


 Optional<TechTalentUser> findByUser(User user);

  TechTalentUser findByTechId(Long techId);

}