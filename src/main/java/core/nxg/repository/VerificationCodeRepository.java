package core.nxg.repository;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


public interface VerificationCodeRepository extends MongoRepository<VerificationCode, String> {
    Optional<VerificationCode> findByCode(String code);
    VerificationCode findByUser(User user);
    
    
}
