package core.nxg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import core.nxg.entity.Role;

import core.nxg.entity.TechTalent;
import core.nxg.repository.TechTalentRepository;
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
import java.util.List;

@Service
public class techTalentService {
    
    private final UserRepository userRepository;
   
    private final TechTalentRepository techTalentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
        
    }

    public techTalentService(TechTalentRepository   techTalentRepository,
                         
                                UserRepository userRepository, 
                         
                                PasswordEncoder passwordEncoder) {
                this.userRepository = userRepository;
                this.techTalentRepository = techTalentRepository;
    }


//////////////////////////////////////////////////////////////////////////////////////////
        // CODE FOR GENERAL USERS STAYS THE SAME
/////////////////////////////////////////////////////////////////////////////////////////

    public User registerTechTalentUser(TechTalent user) throws Exception {


        // Check if a user with the same email already exists
        if (techTalentRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception ("Email already in use");
        }

        // Hash the password before saving it to the database
        user.setPassword(encodePassword(user.getPassword()));

        user.setUserType(Role.TECHTALENT); // You can set this as needed
        return userRepository.save(user);

    }
    
    public List<TechTalent> getUsers() {
        return techTalentRepository.findAll();
        
}
}
