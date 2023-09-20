package core.nxg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import core.nxg.entity.Role;
import core.nxg.dto.fileUploadDTO;
import core.nxg.entity.TechTalent;
import core.nxg.repository.TechTalentRepository;
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
import java.util.List;

@Service
public class techTalentService {
    
    private final UserRepository userRepository;

   
    @Autowired
    private final TechTalentRepository techTalentRepository;
    
   

    public techTalentService(TechTalentRepository   techTalentRepository,
                         
                                UserRepository userRepository, 

                                fileUploadDTO fileUploadDTO
                         
                                 ) {
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
        // unhashed password is set.
        user.setPassword(user.getPassword());

        user.setUserType(Role.TECHTALENT); 
        return userRepository.save(user);

    }
    
    public List<TechTalent> getUsers() {
        return techTalentRepository.findAll();
        
}
}
