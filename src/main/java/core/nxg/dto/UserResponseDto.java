package core.nxg.dto;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
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
        private TechTalentUser techTalentUser;


}