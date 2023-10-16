package core.nxg.repository;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByCode(String code);
    VerificationCode findByUser(User user);
    
    
}
