package core.nxg.serviceImpl;

import core.nxg.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import core.nxg.dto.UserDTO;    
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository){
                this.userRepository = userRepository;
}
    @Override
    public User createUser(UserDTO userDTO) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new Exception("User already exists.");
        }
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setGender(userDTO.getGender());
        user.setLastName(userDTO.getLastName());
        user.setUserType(userDTO.getUserType());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setPassword(userDTO.getPassword());
        return userRepository.saveAndFlush(user);


    }

    @Override
    public List<User> getAllUsers(){

        List<User> users = userRepository.findAll();
        return users;

    }




    
}
