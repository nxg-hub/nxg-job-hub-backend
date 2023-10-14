//package core.nxg.controller;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.*;
//
//import core.nxg.configs.JwtService;
//import core.nxg.dto.LoginDTO;
//import core.nxg.exceptions.AccountExpiredException;
//import core.nxg.exceptions.UserNotFoundException;
//import core.nxg.serviceImpl.UserServiceImpl;
//
//@RestController
//@RequestMapping("/api/v1/auth")
//public class AuthController {
//
//    @Autowired
//    JwtService jwt;
//
//
//    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) throws Exception {
//        try {
//
//            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.login(loginDTO));
//        } catch (AccountExpiredException e) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        } catch (UserNotFoundException e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        } catch (Exception e){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Wrong username or passwoed!");
//        }
//
//
//
//    }
//
//}
