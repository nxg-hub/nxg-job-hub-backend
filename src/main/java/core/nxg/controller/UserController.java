package core.nxg.controller;

import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.response.JobPostingResponse;
import core.nxg.service.UserService;
import core.nxg.serviceImpl.UserServiceImpl;
import core.nxg.utils.Helper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {



    private static final String LOGIN_URL = "https://www.nxgjobhub.com/login";
    @Autowired
    private Helper helper;

    @Autowired
    private final UserService userService;


    @Operation(summary = "REGISTER A NEW USER. THIS IS THE POINT OF ENTRY OF THE APPLICATION")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created the User instance",
                    content = { @Content(
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content),
             })
    @PostMapping("/register/")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO, HttpServletRequest request) throws Exception{
        try {
            String response = userService.createUser(userDTO, helper.getSiteURL(request));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }catch (Exception e) {

            log.error("Error creating new User: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
}



    @Operation(summary = "GET THE LOGGED IN USER INSTANCE .", description ="THIS CONTAINS ONLY COMMON ATTRIBUTES AMONGST EVERY " +
            "USER INSTANCE IN THE APPLICATION")
    @GetMapping("/get-user")
    public ResponseEntity<UserResponseDto> getLoggedInUser(HttpServletRequest request){
        try {
            UserResponseDto response = userService.getLoggedInUser(request);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);        }
    }

    @Operation(summary = "Upload a new photo for a loggedIn User .",
            description ="The jwt is to be passed in the header")
    @PostMapping("/upload-photo")
    public ResponseEntity<Object> uploadPhoto(@RequestBody Map<String,Object> payload, HttpServletRequest request) throws Exception{
        try
        {
            userService.uploadPhoto(payload.get("link").toString(), request);

            return new ResponseEntity<>("Photo uploaded successfully",HttpStatus.OK);
        }catch (Exception ex){
            log.warn(ex.getMessage());
            return  ResponseEntity.badRequest().body("Error uploading photo :" + ex.getMessage());
        }
    }


}