package core.nxg.serviceImpl;

import core.nxg.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;    
import core.nxg.entity.User;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.repository.UserRepository;
//import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService<UserDTO> {
    @Autowired
    private final UserRepository userRepository;

     @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with email already exists.");
        }
        User user = new User();
        user.setPassword(encodePassword(user.getPassword()));

        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setGender(userDTO.getGender());
        user.setLastName(userDTO.getLastName());
        user.setUserType(userDTO.getUserType());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfilePicture(userDTO.getProfilePicture());
        userRepository.saveAndFlush(user);
        return userDTO;


    }


    @Override
    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);}

        //Page<User> users = userRepository.findAll();
    @Override
    public User login(LoginDTO loginDTO) throws Exception {
        Optional<User> user = userRepository.findByEmail(loginDTO.getUsername());
        if (user.isPresent()) {
            if (passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
                return user.get();
            } else {
                throw new Exception("Invalid username or password");
            }
        } else {
            throw new Exception("Invalid username or password");
        }
    
    
}

}