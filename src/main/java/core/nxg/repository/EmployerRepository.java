package core.nxg.repository;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.entity.User;
import core.nxg.response.EmployerResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EmployerRepository extends MongoRepository<Employer, String> {

    Optional<EmployerResponse> findByEmail(String email);



    Optional<EmployerDto> findByUser(User user);
}
