package core.nxg.dto;
import core.nxg.entity.User;
import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class UserResponseDto {

        private String id;
        private String firstName;
        private String lastName;
        private String profilePicture;
        private Gender gender;
        private String phoneNumber;
        private String email;
        private String nationality;
        private LocalDate dateOfBirth;
        private UserType userType;


}