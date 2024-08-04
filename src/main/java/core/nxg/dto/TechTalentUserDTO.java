package core.nxg.dto;

import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TechTalentUserDTO {
    private TechTalentUser techTalentUser;
    private User user;
}
