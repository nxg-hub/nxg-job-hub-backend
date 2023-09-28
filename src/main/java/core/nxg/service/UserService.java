package core.nxg.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import org.springframework.data.domain.Pageable;

@Service
public interface  UserService<T> {
    T createUser(UserDTO userDto) throws Exception; //Should return UserDto instead of user
    Page<User> getAllUsers(Pageable pageable); //Should be a pageable and also return Pageable of userdto
    User login(LoginDTO loginDto) throws Exception;
}
