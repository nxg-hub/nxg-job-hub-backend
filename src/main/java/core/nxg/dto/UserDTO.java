package core.nxg.dto;



import lombok.*;
import core.nxg.enums.Gender;

import java.util.Date;
import java.util.List;
import core.nxg.entity.User;



@Data
@RequiredArgsConstructor
public class UserDTO {
    private String username;
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
        this.username = user.getEmail();
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

