//package core.nxg.serviceImpl;
//
//import core.nxg.service.UserService;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import core.nxg.configs.JwtService;
//import core.nxg.dto.LoginDTO;
//import core.nxg.dto.UserDTO;
//import core.nxg.entity.User;
//import core.nxg.exceptions.AccountExpiredException;
//import core.nxg.exceptions.UserAlreadyExistException;
//import core.nxg.exceptions.UserNotFoundException;
//import core.nxg.repository.UserRepository;
////import java.util.List;
//import java.util.Optional;
//
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import core.nxg.entity.UserInfoDetails;
//
//@Service
//@RequiredArgsConstructor
//public class UserServiceImpl implements UserService<UserDTO> {
//    @Autowired
//    private final UserRepository userRepository;
//
//
//    @Autowired
//    private final JwtService jwt;
//
//    @Autowired
//    private PasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    public String encodePassword(String password) {
//        return encoder.encode(password);
//
//    }
//
//    @Override
//    public String createUser(UserDTO userDTO) throws Exception {
//
//        Optional<User> existingUser = userRepository.findByEmail(userDTO.getUsername());
//        if (existingUser.isPresent()) {
//            throw new UserAlreadyExistException("User with email already exists.");
//        }
//        User user = new User();
//        System.out.println("Creating new user and setting paswword!!");
//        user.setPassword(encodePassword(userDTO.getPassword()));
//
//        user.setEmail(userDTO.getUsername());
//        user.setFirstName(userDTO.getFirstName());
//        user.setGender(userDTO.getGender());
//        user.setLastName(userDTO.getLastName());
//        user.setRoles(userDTO.getRoles());
//        user.setPhoneNumber(userDTO.getPhoneNumber());
//        user.setProfilePicture(userDTO.getProfilePicture());
//        System.out.println("Successfully created ");
//
//        userRepository.saveAndFlush(user);
//        return "User saved Successfully";
//
//
//    }
//
//
//    @Override
//    public Page<User> getAllUsers(Pageable pageable){
//        return userRepository.findAll(pageable);}
//
//        //Page<User> users = userRepository.findAll();
//    @Override
//    public String login(LoginDTO loginDTO) throws Exception {
//
//        Optional<User> user = userRepository.findByEmail(loginDTO.getUsername()) ;
//        UserInfoDetails userInfoDetails = new UserInfoDetails(user.get());
//        if (!user.isPresent()) {
//            throw new UsernameNotFoundException( "Wrong username or password!");
//
//         }
//        if (!encoder.matches(loginDTO.getPassword(), user.get().getPassword())){
//            throw new UserNotFoundException("Wrong username or password!");
//        }
//        if (!userInfoDetails.isEnabled()) {
//            throw new UsernameNotFoundException( "User account is not enabled!");}
//
//         else {
//            String token = jwt.generateToken(new UserInfoDetails(user.get()));
//            loginDTO.setToken(token);
//            return loginDTO.getToken();
//
//         }
//    }
//}
