package core.nxg.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;

@Service
public interface UserService {
    String createUser(UserDTO userDto, String siteURL) throws Exception;
    Page<UserResponseDto> getAllUsers(Pageable pageable);
    UserResponseDto getUserById(Long id) throws Exception;
    String updateUser(Long id, UserResponseDto userDto) throws Exception;
    String deleteUser(Long id) throws Exception;
    String login(LoginDTO loginDTO) throws Exception;

    UserResponseDto getLoggedInUser(HttpServletRequest request) throws Exception;

}