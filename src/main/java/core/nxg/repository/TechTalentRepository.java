package core.nxg.repository;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import core.nxg.entity.TechTalentUser;


@Repository
public interface TechTalentRepository extends MongoRepository<TechTalentUser, String> {

Optional<TechTalentUser>findByEmail(String email);
 List<TechTalentUser> findByAccountCreationDateAfter(LocalDateTime date);
 Optional<TechTalentDTO> findByUser(User user);
 long countByIsVerifiedTrue();
 long countByIsVerifiedFalse();





}