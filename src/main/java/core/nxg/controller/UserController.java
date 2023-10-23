package core.nxg.controller;

import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.serviceImpl.UserServiceImpl;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {



    private final Logger logError = LoggerFactory.getLogger(UserController.class);

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private Helper helper;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/register/")
    public ResponseEntity<String> addUser(@RequestBody UserDTO userDTO, HttpServletRequest request) throws Exception{
        try {
            String response =  userService.createUser(userDTO,helper.getSiteURL(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistException e) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());


        }  catch (Exception e) {
                logError.error("Error creating new User: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("Oops! Something went wrong. Please try again!");
            }




    }

//    @GetMapping("/users/")
//    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
//        try {
//            Page<UserResponseDto> users = userService.getAllUsers(pageable);
//            return new ResponseEntity<>(users, HttpStatus.OK);
//        } catch (Exception e) {
//            logError.error("Error caused by: {}", e.getMessage());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
