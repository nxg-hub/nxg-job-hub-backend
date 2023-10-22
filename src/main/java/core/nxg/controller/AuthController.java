package core.nxg.controller;


import core.nxg.dto.LoginDTO;
import core.nxg.exceptions.AccountExpiredException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.service.EmailService;
import core.nxg.serviceImpl.UserServiceImpl;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmailService emailService;

    @Autowired
    private final UserServiceImpl userService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);



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

    @GetMapping("/confirm-email")
    public String verifyUser(@Nonnull @RequestParam("code") String code, Model model) throws Exception{
        try{
            emailService.confirmVerificationEmail(code);
            return "EmailVerified";
        }catch (Exception e){

            logger.error("Error while verifying email: " + e.getMessage());
            return "Expiredlink";

        }

    }
}








