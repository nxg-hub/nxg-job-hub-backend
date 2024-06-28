package core.nxg.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.UUID;

import java.time.Instant;
import java.time.temporal.ChronoUnit;




@Getter
@Setter
@Document(collection = "verification_code")
public class VerificationCode {

    @Id
    private String id;

    private String code;

//    yToOne(targetEntity = User.class ,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @PrimaryKeyJoinColumn
    private User user;

//    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

//    @Temporal(TemporalType.TIMESTAMP)
    private Instant expiryDate;
 

//    @Column(name = "is_expired")
    public boolean isExpired() {
    if (expiryDate == null) {
        return true;
    }
    return Instant.now().isAfter(expiryDate);}
  

    public VerificationCode(User user) {
        this.code = UUID.randomUUID().toString();
        this.user = user;
        this.createdDate = new Date();
        this.expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);


    }






    
}
