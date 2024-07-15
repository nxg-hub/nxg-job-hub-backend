package core.nxg.controller;


import core.nxg.dto.inAppPasswordResetDTO;
import core.nxg.dto.LoginDTO;
import core.nxg.dto.passwordResetDTO;
import core.nxg.entity.User;
import core.nxg.repository.UserRepository;
import core.nxg.service.EmailService;
import core.nxg.service.PasswordReset;
import core.nxg.service.UserService;

import core.nxg.utils.Helper;

import lombok.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private static final String BASE_URL = "https://nxgjobhub.com";
    private static final String LOGIN_URL = BASE_URL + "/login";


    @Autowired
    private final PasswordReset passwordReset;

    @Autowired
    private Helper helper;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Operation(summary = "Resend a verification email to user",
            description = "Resend a verification email " +
                    "to an unverified user. Could be a NEW user or an" +
                    " EXISTING user who has not verified their email. Email will not be delivered if user is already verified " +
                    "or does not EXIST.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email resent successfully",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid email",
                    content = @Content)})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Email of user to resend verification email to", required = true,
            content = @Content(schema = @Schema(implementation = String.class)))
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
////    @RequestBody(description = "Login credentials", required = true,
////            content = @Content(schema = @Schema(implementation = LoginDTO.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in, " +
                    "returned a jwt token. Check header for token",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid login parameters",
                    content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) throws Exception{
        try {
            String token = userService.login(loginDTO);

            // Retrieve user information after successful login
            Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail()); // Example method to find user

            // Track login activity
            userService.trackLoginActivity(userOptional.get()); // Assuming trackLoginActivity method is in the service layer
           return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer "
            + token)
            .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
            "Authorization")
            .build();

        } catch (Exception e) {
            logger.error("Error while logging in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());}
    }

    @Operation(summary = "Logout a user with user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged out user"
                    ,
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid user id",
                    content = @Content)})
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String userId) {
        userService.logout(userId);

        // Retrieve user information after successful login
        Optional<User> userOptional = userRepository.findById(userId); // Example method to
        // Track login activity
        userService.trackLogoutActivity(userOptional.get());
        return ResponseEntity.ok("User loggedout successfully");
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
    @PostMapping("/reset-password-email/{email}")
    @ResponseBody
    public ResponseEntity<String> sendResetPasswordEmail(@PathVariable String email, HttpServletRequest request) throws Exception {
        try {
            emailService.sendPasswordResetEmail(email, request);
            return ResponseEntity.status(HttpStatus.OK).body("Reset password link sent successfully");
        } catch (Exception e) {
            logger.error("Error while sending reset password email: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid! Please try again!");
        }
    }


    @GetMapping("/reset-password")
    public String resetPassword(@Validated @RequestParam("code") String code, Model model) throws Exception{
        try {
            var token = passwordReset.confirmReset(code);
            return "redirect:" + BASE_URL + "/auth/reset-password?token=" +token;}
        catch(Exception e){
            logger.error("Error while confirming reset link: " + e.getMessage());
            return "ExpiredLink";
        }

    }

    @Operation(summary = "Reset password with new and confirm password")
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
        try {
            passwordReset.updatePassword(dto, request);
            return new ResponseEntity<>( "Password reset successful!", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while resetting password: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }

    @Operation(summary = "Reset password with an old, new and confirm password +" +
            ". Password must contain at least 8 characters, a number, a special character and an uppercase letter.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully reset password",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = inAppPasswordResetDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid reset parameters or conditions.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    @PostMapping("/update-password/in-app")
    @ResponseBody
    public ResponseEntity<String> inAppResetPassword(@RequestBody inAppPasswordResetDTO dto, HttpServletRequest request) throws Exception {
        try {
            passwordReset.inAppPasswordUpdate(dto, request);
            return new ResponseEntity<>( "Password reset successful!", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error while resetting password: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        }
    }


}








