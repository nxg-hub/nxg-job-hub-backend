package core.nxg.repository;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Long> {

}
