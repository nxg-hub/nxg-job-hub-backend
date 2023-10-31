package core.nxg.repository;

import core.nxg.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findByEmailAndOtpAndExpiryTimeAfter(String email, String otp, LocalDateTime expiryTime);
}