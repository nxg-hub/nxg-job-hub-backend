package core.nxg.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TechTalentAgentDto {
    private Long id;
    private String username;
    private String Email;
    private String Phone;
    private String Password;
    private String RetypePassword;
}

