package core.nxg.repository;

import core.nxg.entity.OTP;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface OTPRepository extends MongoRepository<OTP, String> {
    OTP findByEmailAndOtpAndExpiryTimeAfter(String email, String otp, LocalDateTime expiryTime);
}