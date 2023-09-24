package core.nxg.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
public class TechTalentAgentDto {
    private String agentID;
    private String userID;
    private String gender;
    private String jobType;
    private String industryType;
    private String address;
    private String zipCode;
}

