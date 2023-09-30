package core.nxg.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import core.nxg.configs.JwtService;
import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.serviceImpl.UserServiceImpl;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    JwtService jwt;


    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO loginDTO) throws Exception {

        return userService.login(loginDTO);
       
    }
    @PostMapping("/register")
    public String addUser(@RequestBody UserDTO loginDTO)throws Exception{
        return userService.createUser(loginDTO);

    }

}
