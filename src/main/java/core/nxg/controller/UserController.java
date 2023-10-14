//package core.nxg.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import core.nxg.serviceImpl.UserServiceImpl;
//import core.nxg.configs.JwtService;
//import core.nxg.dto.UserDTO;
//import core.nxg.exceptions.UserAlreadyExistException;
//import org.springframework.web.bind.annotation.RestController;
//@RestController
//@RequestMapping("/api/v1/auth")
//public class UserController {
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
//    @PostMapping("/register/")
//    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO) throws Exception{
//        try {
//            String response =  userService.createUser(userDTO);
//            return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (UserAlreadyExistException e) {
//
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//
//
//        }  catch (Exception e) {
//                e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Oops! Something went wrong. Please try again!");
//            }
//
//
//
//    }
//}
