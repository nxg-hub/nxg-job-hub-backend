package core.nxg.serviceImpl;

import core.nxg.configs.JwtService;
import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.entity.UserInfoDetails;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationCodeRepository verificationRepo;


    @Autowired
    private final JwtService jwt;

    @Autowired
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encodePassword(String password) {
        return encoder.encode(password);
    
    }

    @Override
    public String createUser(UserDTO userDTO) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with email already exists.");
        }
        User user = new User();
        user.setPassword(encodePassword(userDTO.getPassword()));
        // Check the UserType and perform the logic of creating a techtalent,aget or emplyer account

        user.setEmail(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setGender(userDTO.getGender());
        user.setLastName(userDTO.getLastName());
        user.setRoles(userDTO.getRoles());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfilePicture(userDTO.getProfilePicture());

        
        userRepository.saveAndFlush(user);
        VerificationCode verificationCode = new VerificationCode(user);
//        verificationCode.setUser(user);
        verificationRepo.saveAndFlush(verificationCode);

        return "User saved Successfully";


    }






    @Override
    public UserResponseDto getUserById(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserResponseDto(user);
    }

    @Override
    public String updateUser(Long id, UserDTO userDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setFirstName(userDto.getFirstName());
        user.setEmail(userDto.getUsername());
        userRepository.save(user);
        return "User updated successfully";
     
    }

    @Override
    public String deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
        return "User deleted successfully";    }




    @Override
    public String login(LoginDTO loginDTO) throws Exception {
        
        Optional<User> user = userRepository.findByEmail(loginDTO.getUsername()) ;
        if (user.isEmpty()){ throw new UsernameNotFoundException("Username does not exist");}
        UserInfoDetails userInfoDetails = new UserInfoDetails(user.get());

        if (!encoder.matches(loginDTO.getPassword(), user.get().getPassword())){
            throw new UserNotFoundException("Wrong username or password!");
        }
        if (!userInfoDetails.isEnabled()) {
            throw new UsernameNotFoundException( "User account is not enabled!");}

         else {
            String token = jwt.generateToken(new UserInfoDetails(user.get()));
            loginDTO.setToken(token);
            return loginDTO.getToken();
             
         }
    }

    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponseDto::new);

    }
}
