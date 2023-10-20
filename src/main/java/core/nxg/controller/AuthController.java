package core.nxg.controller;


import core.nxg.dto.EmailDTO;
import core.nxg.dto.LoginDTO;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.AccountExpiredException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import core.nxg.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    



    @Autowired
    EmailService emailService;

    @Autowired
    private final UserServiceImpl userService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationCodeRepository verificationRepo;

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws Exception {
        try {
             
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.login(loginDTO));
        } catch (AccountExpiredException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong username or password!");
        }

         
       
    }
    @ResponseBody
    @PostMapping("/verify-by-email")
    public String verifyEmail( @RequestBody EmailDTO dto, HttpServletRequest request) {
        try {
            emailService.sendVerificationEmail(dto, getSiteURL(request));
            return "Email verification sent successfully!";
        } catch (Exception e) {
            logger.error("Error sending email: {}", e.getMessage());
            return "Error sending email: "+ e.getMessage();
        }
    }
        private String getSiteURL(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toString();
    }



    @GetMapping("/confirm-email")
    public String verifyUser(@NonNull @RequestParam("code") String code, Model model) throws NoSuchElementException, Exception {
        try {
            Optional<VerificationCode> verificationCode = verificationRepo.findByCode(code);
            if (verificationCode.isPresent()) {
                VerificationCode verification = verificationCode.get();
                if (verification.isExpired()) {
                    return "Expiredlink";
                }else {
                    verification.getUser().setEnabled(true);
                    userRepository.save(verification.getUser());
                    verificationRepo.deleteById(verification.getId());
                    /* TODO : MAKE THIS A SERVICE TO ENCAPSULATE THIS OPERATION */

                    return "EmailVerified";
                }
            }else {
                return "Expiredlink";          }
        } catch (NoSuchElementException e) {
            return "Expiredlink";    }
}
}



