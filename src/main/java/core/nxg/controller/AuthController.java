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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.awt.print.Book;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final String LOGIN_URL = "http://localhost:3000/login";

    @Autowired
    private final PasswordReset passwordReset;

    @Autowired
    private Helper helper;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserServiceImpl userService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Operation(summary = "Resend a verification email to user",
            description = "Resend a verification email " +
                    "to an unverified user. Could be a NEW user or an" +
                    " EXISTING user who has not verified their email. Email will not be delivered if user is already verified " +
                    "or does not EXIST.")
    @PostMapping("/resendverification-mail")
    @ResponseBody
    public ResponseEntity<String> resend(@RequestParam String email, HttpServletRequest request){
        try{
            emailService.reSendVerificationEmail(email, helper.getSiteURL(request));
            return ResponseEntity.ok("Email resent successfully");
        }catch(Exception e){
            logger.error(e.getMessage());
            return null;
        }
    }

    @Operation(summary = "Login a user with email and password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in, " +
                    "returned a jwt token. Check header for token",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid login parameters",
                    content = @Content)})
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws Exception{
        try {
            String token = userService.login(loginDTO);
           return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " 
            + token)
            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            "Authorization")
            .build();
            // return ResponseEntity.status(HttpStatus.OK).body("Login successful");

        } catch (Exception e) {
            logger.error("Error while logging in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());}
    }



    @GetMapping("/confirm-email")
    public String verifyUser(@Nonnull @RequestParam("code") String code, Model model) throws Exception{
        model.addAttribute("loginUrl", LOGIN_URL);
        try {
            emailService.confirmVerification(code);

            return "EmailVerified";
        } catch (Exception e) {
            logger.error("Error while verifying email: " + e.getMessage());
            return "ExpiredLink";
        }
    }

    @Operation(summary = "Send a password reset email",
    description = "Send a password reset email to a user with the email address provided. " +
            "Email will not be delivered if user does not EXIST.")
    @PostMapping("/reset-password-email")
    @ResponseBody
    public ResponseEntity<String> sendResetPasswordEmail(@RequestBody EmailDTO dto, HttpServletRequest request) throws Exception {
        try {
            emailService.sendPasswordResetEmail(dto, helper.getSiteURL(request), request);
            return ResponseEntity.status(HttpStatus.OK).body("Reset password link sent successfully");
        } catch (Exception e) {
            logger.error("Error while sending reset password email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid! Please try again!");
        }
    }


    @GetMapping("/reset-password")
    public String resetPassword(@Nonnull @RequestParam("code") String code, Model model) throws Exception{
        try {
            emailService.confirmReset(code);
            return "redirect:/api/vi/auth/update-password/";}
        catch(Exception e){
            logger.error("Error while confirming reset link: " + e.getMessage());
            return "ExpiredLink";
        }

    }

    @Operation(summary = "Reset password with old, new and confirm password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully reset password",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = passwordResetDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid reset parameters or conditions.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping("/update-password/")
    @ResponseBody
    public ResponseEntity<String> forgotPassword(@RequestBody passwordResetDTO dto, HttpServletRequest request) throws Exception {
        try {passwordReset.updatePassword(dto, request);
            return ResponseEntity.status(HttpStatus.OK).body("Password reset successfully");
        } catch (Exception e) {
            logger.error("Error while resetting password: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());

        }
    }



}








