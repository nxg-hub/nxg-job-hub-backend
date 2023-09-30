package core.nxg.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import core.nxg.enums.Gender;
import core.nxg.enums.UserType;
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;

public class UserServiceImplTests {

    private UserRepository userRepository;
   
   

    @Test
    private void createUser() {
        User user = new User();
        user.setEmail("jondoe@gmail.com");
        user.setGender(Gender.FEMALE);
        user.setLastName("Jon");
        user.setLastName("doe");
        user.setPhoneNumber("+23480806578");
        //user.setSkills(List.of(("Java"),("Python"),("SQL")));
        //user.setUser(new("john.doe@example.com"));
       // user.setUserType(UserType.ADMIN);
        user.setPassword("password");
        user.setProfilePicture("/path/to/profilepicture.jpg");
        user.setDateOfBirth(LocalDateTime.of(1997, 2, 2, 3, 16, 3));

            
        userRepository.save(user);
        assertNotNull(user);
    }  
  
    }
