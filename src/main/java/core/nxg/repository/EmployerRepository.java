package core.nxg.repository;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

    Optional<EmployerDto> findByEmail(String email);



    Optional<EmployerDto> findByUser(User user);
}
