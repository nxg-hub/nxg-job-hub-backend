package core.nxg.dto;


import core.nxg.entity.User;
import core.nxg.enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;



@Setter
@Getter
@RequiredArgsConstructor
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String profilePicture;
    private Gender gender;
    private String roles;
    private LocalDate dateOfBirth;
    private String Nationality;

    public UserDTO(User user) {

    }
}

