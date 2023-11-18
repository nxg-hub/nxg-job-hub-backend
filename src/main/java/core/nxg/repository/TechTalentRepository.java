package core.nxg.repository;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import core.nxg.entity.TechTalentUser;


@Repository
public interface TechTalentRepository extends JpaRepository<TechTalentUser, Long> {


 Optional<TechTalentDTO> findByUser(User user);





}