package core.nxg.dto;

import core.nxg.entity.TechTalentAgent;
import core.nxg.entity.User;
import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import lombok.Data;

import java.util.Date;

@Data
public class UserResponseDto {

        private Long id;
        private String firstName;
        private String lastName;
        private String profilePicture;
        private Gender gender;
        private String phoneNumber;
        private String email;
        private Date dateOfBirth;
        private UserType userType;
        private TechTalentAgent createdAgent;

        // Constructor to map values from User entity to DTO
        public UserResponseDto(User user) {
                this.id = user.getId();
                this.firstName = user.getFirstName();
                this.lastName = user.getLastName();
                this.profilePicture = user.getProfilePicture();
                this.gender = user.getGender();
                this.phoneNumber = user.getPhoneNumber();
                this.email = user.getEmail();
                this.dateOfBirth = user.getDateOfBirth();
                this.userType = user.getUserType();
        }


}
