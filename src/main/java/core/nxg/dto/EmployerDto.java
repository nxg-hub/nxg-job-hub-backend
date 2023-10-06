package core.nxg.dto;

import lombok.Getter;
import lombok.Setter;
import core.nxg.entity.User;

@Getter
@Setter
public class EmployerDto<T> {
    private String employerID;
    private String email;
    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;
    private String companyWebsite;
    private String country;
    private String industryType;
    private String companySize;
    private String ratings;

    public User setUser(User user) {
        return user;

    }
   
}
