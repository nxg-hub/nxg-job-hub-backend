package core.nxg.serviceImpl;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.VerificationCode;
import core.nxg.enums.Provider;
import core.nxg.exceptions.*;
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
import core.nxg.repository.UserRepository;
//import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;


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
    private final Helper helper;

    @Autowired
    private final ModelMapper modelMapper;


    @Autowired
    private final VerificationCodeRepository verificationRepo;

    @Override
    public String createUser(UserDTO userDTO, String siteURL) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (helper.EmailIsInvalid(userDTO.getEmail())) {
            throw new EmailNotValidException("Invalid email address!");
        }
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException("User with email already exists!");
        }

        User user = new User();


        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setGender(userDTO.getGender());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setDateOfBirth(userDTO.getDateOfBirth());
        user.setNationality(userDTO.getNationality());
        user.setProvider(Provider.LOCAL);
        user.setPassword(helper.encodePassword(userDTO.getPassword()));

        VerificationCode verificationCode = new VerificationCode(user);
        emailService.sendVerificationEmail(verificationCode, siteURL);
        userRepository.saveAndFlush(user);
        verificationRepo.saveAndFlush(verificationCode);

        return "User saved Successfully";


    }

    @Override
    public void createOAuthUSer(String username, String password, String provider) {
        User existinguser = userRepository.findByEmailAndProvider(username,Provider.valueOf(provider.toUpperCase() ))
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        User user = new User();
        user.setEmail(username);
        user.setProvider(Provider.valueOf(provider.toUpperCase()));
        user.setPassword(helper.encodePassword(password));
        //TODO: SEND SUCCESS EMAIL FOR OAUTH REGISTRATION
        userRepository.save(user);

    }

    @Override
    public String login( LoginDTO loginDTO) throws Exception {

        Optional<User> user = userRepository.findByEmail(loginDTO.getEmail());

        if (helper.EmailIsInvalid(loginDTO.getEmail())){
            throw new EmailNotValidException("Invalid email address");
    }
        if (user.isEmpty()) {
            throw new UsernameNotFoundException( "Wrong username or password!");

         }

        if (!helper.isPasswordValid(loginDTO.getPassword(), user.get().getPassword())){
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
    public UserResponseDto getLoggedInUser(HttpServletRequest request) throws Exception {
    User user  = helper.extractLoggedInUser(request);
    return modelMapper.map(user, UserResponseDto.class);
    }






    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> user = userRepository.findAll(pageable);
        return user.map(u -> modelMapper.map(u, UserResponseDto.class));
    }

}


