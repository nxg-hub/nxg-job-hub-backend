package core.nxg.utils;


import core.nxg.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "header_signature")
public class HeaderSignature {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "signature")
    private String signature;


    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt ;

//    private List<User> implementers;

    public HeaderSignature(SecretKey value) {

        this.signature = Base64.getEncoder().encodeToString(value.getEncoded());
        this. createdAt = LocalDateTime.now();

    }
}
