package core.nxg.repository;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.nxg.entity.TechTalentUser;


@Repository
public interface TechTalentRepository extends JpaRepository<TechTalentUser, Long> {


 Optional<TechTalentUser> findByUser(User user);
 


}