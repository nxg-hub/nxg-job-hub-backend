package core.nxg.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "not_verified_talents")
public class NotVerifiedEmployers {

    @Id
    private String id;
    private String email;
    private String industryType;
    private String employerName;
    private LocalDateTime dateJoined;
}
