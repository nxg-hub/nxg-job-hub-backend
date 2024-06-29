package core.nxg.dto;

import core.nxg.entity.Employer;
import core.nxg.entity.User;
import core.nxg.enums.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingsDto {
//    private String ratingsId;
    private String Id;
    private String rating;


    private String employerID;

    private String raterID;


}
