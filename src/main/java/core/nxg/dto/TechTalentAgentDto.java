package core.nxg.dto;

import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import lombok.*;

@Getter
@Setter
public class TechTalentAgentDto {
    // private String agentID;
    //private String userID;
    //private String gender;
    private JobType jobType;
    private IndustryType industryType;
    private String address;
    private String zipCode;


}

