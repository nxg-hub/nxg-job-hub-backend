package core.nxg.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "new_employer_users")
public class NewEmployerUsers {

    @Id
    private String id;
    private String email;
    private String employerName;
    private String industryType;
    private LocalDateTime dateJoined;
}