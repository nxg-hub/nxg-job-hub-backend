package core.nxg.controller;
import core.nxg.entity.TechTalent;
//import core.nxg.entity.User;
//import core.nxg.repository.UserRepository;
import core.nxg.service.UserService;
import core.nxg.service.techTalentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {
    //@Autowired
    //private final UserService userService;
    @Autowired
    private final techTalentService techTalentService;
    
    public AuthController(UserService userService, techTalentService techTalentService) {
      //  this.userService = userService;
        this.techTalentService = techTalentService;
    }


    //////////////////////////////////////////////////////////////////////////////////////
                /// POST MAPPING FOR USERS. STAYS THE SAME
    ///////////////////////////////////////////////////////////////////////////////////////
                /// GET MAPPING FOR USERS. sTAYS THE SAME
    //////////////////////////////////// /////////////////////////////////////////////////


    @PostMapping("/register/tech-talent")
    public ResponseEntity<String> createTechTalentUser(@RequestBody TechTalent user) {
        try {
            techTalentService.registerTechTalentUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return new ResponseEntity<>("INVALID REQUEST", HttpStatus.BAD_REQUEST);

        }
    
    }
    @GetMapping("/user/tech-talent")
    public ResponseEntity<List<TechTalent>> getTechTalentUsers() {
        try {
            List<TechTalent> users = techTalentService.getUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }   

}

