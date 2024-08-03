package core.nxg.repository;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.response.EmployerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmployerRepository extends MongoRepository<Employer, String> {

    Optional<EmployerResponse> findByEmail(String email);
    Optional<EmployerDto> findByUser(User user);
    List<Employer> findByAccountCreationDateAfter(LocalDateTime date);
    long countByVerifiedTrue();
    long countByVerifiedFalse();
}
