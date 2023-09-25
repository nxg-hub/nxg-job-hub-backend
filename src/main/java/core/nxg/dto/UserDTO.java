package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;


import lombok.*;
import core.nxg.enums.UserType;
import core.nxg.enums.Gender;

@Getter
@Setter
public class UserDTO {
    
    private String username;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private Gender gender;
    private UserType userType;
    private String password;
    private String phoneNumber;
    private String dateOfBirth;
    private String Nationality;

}

