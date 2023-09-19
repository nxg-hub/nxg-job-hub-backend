package core.nxg.service;

import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
   
    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    public UserService(PasswordEncoder passwordEncoder,UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User registerUser(User user) throws Exception {
        // Check if a user with the same email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception ("Email already in use");
        }

        // Hash the password before saving it to the database
        user.setPassword(encodePassword(user.getPassword()));


        return userRepository.save(user);
    }
}
