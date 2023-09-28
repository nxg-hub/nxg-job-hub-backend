package core.nxg.repository;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.nxg.entity.TechTalentUser;

@Repository
@Primary
public interface TechTalentRepository extends JpaRepository<TechTalentUser, Long> {

  TechTalentRepository userRepository = new TechTalentRepositoryImpl();

  

  default Optional<User> findByEmail(String email) {
    Optional<User> user = userRepository.findByEmail(email);
    return user;
  }
}