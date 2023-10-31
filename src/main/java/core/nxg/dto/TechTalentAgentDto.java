package core.nxg.dto;

import core.nxg.entity.User;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import jakarta.persistence.OneToOne;
import lombok.*;

@Getter
@Setter
public class TechTalentAgentDto {
    private Long id;
    private String email;
    // private String agentID;
//    private String userId;
    //private String gender;
    private JobType jobType;
    private IndustryType industryType;
    private String address;
    private String zipCode;

//    @OneToOne(mappedBy = "techTalentAgent")
//    private User user;
}

