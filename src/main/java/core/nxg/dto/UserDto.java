package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;


import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserDto {
    
    private String username;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String email;
    private String password;
    private String phoneNumber;
    private String dateOfBirth;
    private String Nationality;

}

