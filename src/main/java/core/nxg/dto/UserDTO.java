package core.nxg.dto;


import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;



@Setter
@Getter
@RequiredArgsConstructor
public class UserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nationality;


}

