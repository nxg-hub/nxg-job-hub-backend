package core.nxg.entity;

import lombok.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;



@Data
@RequiredArgsConstructor
@Entity
@Table(name = "verification_code")
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    @OneToOne(targetEntity = User.class ,cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private Instant expiryDate;
 
    private boolean isExpired;

    public boolean isExpired() {
    if (expiryDate == null) {
      /*1000  TODO below*/
        return false; // or return true, depending on your logic
    }
    return Instant.now().isAfter(expiryDate);}
  

    public VerificationCode(User user) {
        this.code = UUID.randomUUID().toString();
        this.user = user;
        this.createdDate = new Date();
        this.expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);


    }






    
}
