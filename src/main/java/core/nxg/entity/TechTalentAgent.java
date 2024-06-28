package core.nxg.entity;

import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;



@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "agents")
public class TechTalentAgent{
    @Id
    private String id;
    private String email;

    private JobType jobType;
    private IndustryType industryType;
    private String address;
    private String zipCode;

//    ToOne
//    @JoinColumn(name = "user_id")
    @DBRef
    private User user;

}
