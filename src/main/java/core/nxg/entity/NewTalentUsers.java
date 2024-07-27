package core.nxg.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "new_talent_users")
public class NewTalentUsers {

    @Id
    private String id;
    private String email;
    private String talentName;
    private String jobInterest;
    private LocalDateTime dateJoined;
}
