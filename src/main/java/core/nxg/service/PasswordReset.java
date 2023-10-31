package core.nxg.service;

import core.nxg.dto.passwordResetDTO;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.utils.Helper;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordReset {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final Helper helper;




    public void resetPassword(passwordResetDTO passwordResetDTO) throws Exception {
        Optional<User> user = userRepository.findByEmail(passwordResetDTO.getEmail());
        if(user.isEmpty()){
            throw new UserNotFoundException("User with email does not exist");}
        String password = passwordResetDTO.getOldPassword();
        if(!helper.encoder.matches(password,user.get().getPassword())){
            throw new Exception("Password is incorrect!");
        }
        if (!passwordResetDTO.getNewPassword().equals(passwordResetDTO.getConfirmPassword())){
            throw new Exception("Passwords do not match!");
        }else{
            user.get().setPassword(helper.encodePassword(passwordResetDTO.getNewPassword()));

            userRepository.save(user.get());
        }
    }
}
