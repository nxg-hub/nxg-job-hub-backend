package core.nxg.serviceImpl;

import core.nxg.configs.oauth2.OAuth2Provider;
import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.VerificationCode;
import core.nxg.enums.Provider;
import core.nxg.enums.UserType;
import core.nxg.exceptions.*;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import core.nxg.service.UserService;

import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import core.nxg.configs.JwtService;
import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
//import java.util.List;
import java.security.SecureRandom;
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
        user.setProvider(OAuth2Provider.LOCAL);
        user.setPassword(helper.encodePassword(userDTO.getPassword()));

        VerificationCode verificationCode = new VerificationCode(user);
        emailService.sendVerificationEmail(verificationCode, siteURL);
        userRepository.saveAndFlush(user);
        verificationRepo.saveAndFlush(verificationCode);

        return "User saved Successfully";


    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public String generateOAuthPassword() {

        // Define the characters to be used for password generation
        final String UPPER_CASE_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+";

            StringBuilder password = new StringBuilder();
            SecureRandom random = new SecureRandom();

            // Add 2 random upper case characters
            for (int i = 0; i < 2; i++) {
                char upperCaseChar = UPPER_CASE_CHARACTERS.charAt(random.nextInt(UPPER_CASE_CHARACTERS.length()));
                password.append(upperCaseChar);
            }

            // Add 2 random special characters
            for (int i = 0; i < 2; i++) {
                char specialChar = SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length()));
                password.append(specialChar);
            }

            // Add 4 random alphanumeric characters
            String alphanumericCharacters = UPPER_CASE_CHARACTERS.toLowerCase() + "0123456789";
            for (int i = 0; i < 4; i++) {
                char randomChar = alphanumericCharacters.charAt(random.nextInt(alphanumericCharacters.length()));
                password.append(randomChar);
            }

            // Shuffle the characters in the password
        return shuffleString(password.toString());
        }

        // Method to shuffle the characters in a string
        private String shuffleString(String string) {
            char[] charArray = string.toCharArray();
            SecureRandom random = new SecureRandom();

            for (int i = 0; i < charArray.length; i++) {
                int randomIndex = random.nextInt(charArray.length);
                char temp = charArray[i];
                charArray[i] = charArray[randomIndex];
                charArray[randomIndex] = temp;
            }

            return new String(charArray);
    }


    @Override
    public void createOAuthUSer(String username, String provider) {
        User existinguser = userRepository.findByEmailAndProvider(username,Provider.valueOf(provider.toUpperCase() ))
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        User user = new User();
        user.setEmail(username);
        user.setProvider(OAuth2Provider.GOOGLE);
//        user.setPassword(helper.encodePassword(password));
        //TODO: SEND SUCCESS EMAIL FOR OAUTH REGISTRATION
        userRepository.save(user);

    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public String login( LoginDTO loginDTO) throws Exception {

        var user = userRepository.findByEmail(loginDTO.getEmail());

        if (helper.EmailIsInvalid(loginDTO.getEmail())){
            throw new EmailNotValidException("Invalid email address");
    }
        if (user.isEmpty()) {
            throw new UserNotFoundException( "Wrong username or password!");

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


    public Page<User> getUsersByType(UserType userType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByUserType(userType, pageable);
    }

    public Page<User> getTalentUsers(int page, int size) {
        return getUsersByType(UserType.TECHTALENT, page, size);
    }

    public Page<User> getAgentUsers(int page, int size) {
        return getUsersByType(UserType.AGENT, page, size);
    }

    public Page<User> getEmployerUsers(int page, int size) {
        return getUsersByType(UserType.EMPLOYER, page, size);
    }

}


