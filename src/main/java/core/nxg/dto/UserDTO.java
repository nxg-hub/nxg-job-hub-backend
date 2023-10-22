package core.nxg.dto;


import core.nxg.entity.User;
import core.nxg.enums.Gender;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;



@Data
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
    private Date dateOfBirth;
    private String Nationality;

    public UserDTO(User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePicture = user.getProfilePicture();
        this.gender = user.getGender();
        this.roles = user.getRoles();
        this.dateOfBirth = user.getDateOfBirth();
        this.Nationality = user.getNationality();

    }
}

