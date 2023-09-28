package core.nxg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import core.nxg.dto.LoginDTO;
import core.nxg.entity.User;
import core.nxg.serviceImpl.UserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO loginDTO) throws Exception {
        User user = userService.login(loginDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
