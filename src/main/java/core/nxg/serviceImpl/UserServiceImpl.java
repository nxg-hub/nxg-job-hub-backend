package core.nxg.serviceImpl;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.EmailNotValidException;
import core.nxg.exceptions.IncorrectDetailsException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import core.nxg.service.UserService;

import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import core.nxg.configs.JwtService;
import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.repository.UserRepository;
//import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
//import core.nxg.entity.UserInfoDetails;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;


    @Autowired
    private final JwtService jwt;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final Helper<String,String> helper;

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private final VerificationCodeRepository verificationRepo;

    @Override
    public String createUser(UserDTO userDTO, String siteURL, HttpServletRequest request) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (!helper.isEmailValid(userDTO.getEmail())) {
            throw new EmailNotValidException("Invalid email address");
        }
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with email already exists!.");
        }

        User user = new User();

        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setGender(userDTO.getGender());
        user.setLastName(userDTO.getLastName());
        user.setRoles(userDTO.getRoles());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setPassword(helper.encodePassword(userDTO.getPassword()));

        VerificationCode verificationCode = new VerificationCode(user);
        emailService.sendVerificationEmail(verificationCode, siteURL, request);
        userRepository.saveAndFlush(user);
        verificationRepo.saveAndFlush(verificationCode);

        return "User saved Successfully";


    }



    @Override
    public String login(LoginDTO loginDTO) throws Exception {

        Optional<User> user = userRepository.findByEmail(loginDTO.getEmail()) ;

        if(!helper.isEmailValid(loginDTO.getEmail())){
            throw new EmailNotValidException("Invalid email address");
        }
        if (user.isEmpty()) {
            throw new UsernameNotFoundException( "Wrong username or password!");

         }

        if (!helper.isPasswordMatch(loginDTO.getPassword(), user.get().getPassword())){
            throw new IncorrectDetailsException("Wrong username or password!");
        }

        if (!user.get().isEnabled()) {
            throw new UsernameNotFoundException( "Account is yet to be verified. Kindly confirm your email!");}

         else {
            return jwt.generateToken(user.get());

         }
    }

    @Override
    public UserResponseDto getUserById(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public String updateUser(Long id, UserDTO userDto) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

        modelMapper.map(userDto, user);
        userRepository.save(user);
        return "User updated successfully";

    }

    @Override
    public String deleteUser(Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
        return "User deleted successfully";
    }


    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> user = userRepository.findAll(pageable);
        return user.map(u -> modelMapper.map(u, UserResponseDto.class));
    }

}


