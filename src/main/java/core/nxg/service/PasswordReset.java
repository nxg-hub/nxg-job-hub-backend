package core.nxg.service;

import core.nxg.configs.JwtService;
import core.nxg.dto.passwordResetDTO;
import core.nxg.entity.User;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.TokenExpiredException;
import core.nxg.exceptions.TokenNotFoundException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.utils.Helper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

    @Autowired
    private final VerificationCodeRepository verificationRepo;

    @Autowired
    private final JwtService jwtService;




    public void updatePassword(@Nonnull passwordResetDTO passwordResetDTO, HttpServletRequest request) throws Exception {
        User loggedInUser = helper.extractLoggedInUser(request);
        Optional<User> user = userRepository.findByEmail(loggedInUser.getEmail());
        if(user.isEmpty()){
            throw new UserNotFoundException("User with email does not exist");}



        if (!passwordResetDTO.getNewPassword().equals(passwordResetDTO.getConfirmPassword())){
            throw new Exception("Passwords do not match!");
        }else{
            
            user.get().setPassword(helper.encodePassword(passwordResetDTO.getNewPassword()));

            userRepository.save(user.get());
        }
    }

    public String confirmReset(String verificationCode) throws Exception {

        Optional<VerificationCode> verification = verificationRepo.findByCode(verificationCode);
        if (verification.isEmpty()) {
            throw new TokenNotFoundException("Invalid reset code!");
        }

        if (verification.get().isExpired()) {
            throw new TokenExpiredException("Verification code has expired!");
        } else {
            userRepository.save(verification.get().getUser());
            verificationRepo.deleteById(verification.get().getId());
            return jwtService.generateToken(verification.get().getUser());
        }
    }
}
