package core.nxg.dto;



import lombok.*;
import core.nxg.enums.Gender;



@Data
@RequiredArgsConstructor
public class UserDTO {
    
    private String username;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private Gender gender;
    private String roles;
    private String password;
    private String phoneNumber;
    private String dateOfBirth;
    private String Nationality;

}

