package core.nxg.controller;


import core.nxg.dto.EmailDTO;
import core.nxg.dto.LoginDTO;
import core.nxg.dto.passwordResetDTO;
import core.nxg.exceptions.AccountExpiredException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.service.EmailService;
import core.nxg.service.PasswordReset;
import core.nxg.serviceImpl.UserServiceImpl;
import core.nxg.utils.Helper;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {


    @Autowired
    private final PasswordReset passwordReset;

    @Autowired
    private Helper helper;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserServiceImpl userService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception{
        try {
            ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " 
            + userService.login(loginDTO))
            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            "Authorization")
            .build();
            return ResponseEntity.status(HttpStatus.OK).body("Login successful");

        } catch (Exception e) {
            logger.error("Error while logging in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());}
    }



    @GetMapping("/confirm-email")
    public String verifyUser(@Nonnull @RequestParam("code") String code, Model model) throws Exception{
        try {
            emailService.confirmVerification(code);
            return "EmailVerified";
        } catch (Exception e) {
            logger.error("Error while verifying email: " + e.getMessage());
            return "ExpiredLink";
        }
    }


    @PostMapping("/reset-password-email")
    @ResponseBody
    /*  SEND A PASSWORD RESET EMAIL */
    public String sendResetPasswordEmail(@RequestBody EmailDTO dto, HttpServletRequest request) throws Exception {
        try {
            emailService.sendPasswordResetEmail(dto, helper.getSiteURL(request));
            return "Reset Email Sent successfully!";
        } catch (Exception e) {
            logger.error("Error while sending reset password email: " + e.getMessage());
            return ("Oops! " + e.getMessage());
        }
    }


    @GetMapping("/reset-password")
    /*  CONFIRM THE RESET PASSWORD REQUEST */
    public String resetPassword(@Nonnull @RequestParam("code") String code, Model model) throws Exception{
        try {
            emailService.confirmReset(code);
            return "redirect:/api/vi/auth/reset-password/confirm";}
        catch(Exception e){
            logger.error("Error while confirming reset link: " + e.getMessage());
            return "ExpiredLink";
        }

    }


    @PostMapping("/reset-password/confirm")
    @ResponseBody
    /* RESET THE PASSWORD WITH OLD , NEW AND A CONFIRM PASSWORD */
    public ResponseEntity<String> forgotPassword(@RequestBody passwordResetDTO dto) throws Exception {
        try {passwordReset.resetPassword(dto);
            return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully");
        } catch (Exception e) {
            logger.error("Error while resetting password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body("Oops! Something went wrong. Please try again!");

        }
    }



}








