package core.nxg.service;

import org.springframework.stereotype.Service;
import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import java.util.List;


@Service
public interface UserService {
    User createUser(UserDTO userDto) throws Exception; //Should return UserDto instead of user
    List<User> getAllUsers(); //Should be a pageable and also return Pageable of userdto
}
