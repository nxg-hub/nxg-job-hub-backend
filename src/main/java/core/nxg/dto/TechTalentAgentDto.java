package core.nxg.dto;

import core.nxg.entity.User;
import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class TechTalentAgentDto {

    private IndustryType industryType;
    private String address;
    private String zipCode;


}

