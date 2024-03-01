package core.nxg.service;

import core.nxg.entity.User;
import core.nxg.enums.Provider;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

@Service
public interface UserService {
    String createUser(UserDTO userDto, String siteURL) throws Exception;
    User saveUser(User user);

    String generateOAuthPassword();

    void createOAuthUSer(String username, String provider);
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long id) throws Exception;
    String login(LoginDTO loginDTO) throws Exception;

    UserResponseDto getLoggedInUser(HttpServletRequest request) throws Exception;

    Optional<User> getUserByUsername(String username);

    Page<User> getTalentUsers(int page, int size);

    Page<User> getAgentUsers(int page, int size);

    Page<User> getEmployerUsers(int page, int size);


}