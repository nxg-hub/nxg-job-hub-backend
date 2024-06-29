package core.nxg.repository;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import core.nxg.entity.TechTalentUser;


@Repository
public interface TechTalentRepository extends MongoRepository<TechTalentUser, String> {

Optional<TechTalentDTO >findByEmail(String email);
 Optional<TechTalentDTO> findByUser(User user);





}