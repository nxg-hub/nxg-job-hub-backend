package core.nxg.entity;



import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "otp")
public class OTP {

    @Id
    private Long id;
    private String email;
    private String otp;
    private String phoneNumber;
    private LocalDateTime expiryTime;}