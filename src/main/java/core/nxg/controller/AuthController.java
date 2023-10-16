package core.nxg.controller;


import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.entity.VerificationCode;
import core.nxg.exceptions.AccountExpiredException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.UserRepository;
import core.nxg.repository.VerificationCodeRepository;
import core.nxg.service.EmailService;
import core.nxg.serviceImpl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    



    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserServiceImpl userService;

    

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationCodeRepository verificationRepo;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws Exception {
        try {
             
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.login(loginDTO));
        } catch (AccountExpiredException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong username or password!");
        }

         
       
    }
    @PostMapping("/verify-by-email")
    /*  TODO REPLACE USERDTO WITH A MORE FLEXIBLE AND SAFE DTO FOR EMAIL VERIFICATION*/
    public ResponseEntity<String> verifyEmail(@Valid @RequestBody UserDTO dto, HttpServletRequest request) throws UnsupportedEncodingException, Exception {
        try {
            emailService.sendVerificationEmail(dto, getSiteURL(request));
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Email verification link sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Oops! Something went wrong. Please try again!");
        }
    }
    private String getSiteURL(HttpServletRequest request) {
    return ServletUriComponentsBuilder.fromRequestUri(request)
            .replacePath(null)
            .build()
            .toString();
}


    @GetMapping("/confirm-email")
    public String verifyUser(@NotNull @RequestParam("code") String code) throws UnsupportedEncodingException, Exception {
     
        Optional<VerificationCode> verificationCode = verificationRepo.findByCode(code);
        VerificationCode verification = verificationCode.get();
        /* TODO : CHECK FOR REAL VERIFICATION CODE WITH EXPIRYDATE SET AND NOT SET */
            if (verificationCode.isEmpty() && verification.isExpired()) {
                return "Expiredlink";
            } else {
                verification.getUser().setEnabled(true);
                userRepository.save(verification.getUser());
    /* TODO : CHECK WHY THYMELEAF IS NOT RETURNING A TEMPLATE VIEW */
                verificationRepo.deleteById(verification.getId());
                return "EmailVerified";
            }
    }
}



