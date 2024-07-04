package core.nxg.utils;


import core.nxg.entity.User;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "header_signature")
public class HeaderSignature {
    @Id
    private String id;

    private String signature;


    private LocalDateTime createdAt ;

//    private List<User> implementers;

    public HeaderSignature(SecretKey value) {

        this.signature = Base64.getEncoder().encodeToString(value.getEncoded());
        this. createdAt = LocalDateTime.now();

    }

}
