package core.nxg.service;

import core.nxg.entity.User;
import core.nxg.exceptions.ExpiredJWTException;
import org.springframework.stereotype.Service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

@Service
public interface UserService {

    void uploadPhoto(String link, HttpServletRequest request) throws ExpiredJWTException;
    String createUser(UserDTO userDto, String siteURL) throws Exception;
    User saveUser(User user);

    String generateOAuthPassword();


    void logout(String userId);

    void trackLoginActivity(User user);

    void trackLogoutActivity(User user);

    UserResponseDto getUserById(String id) throws Exception;
    String login(LoginDTO loginDTO) throws Exception;

    UserResponseDto getLoggedInUser(HttpServletRequest request) throws Exception;

    Optional<User> getUserByUsername(String username);

}