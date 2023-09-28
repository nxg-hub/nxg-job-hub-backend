package core.nxg.controller;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;
import core.nxg.service.TechTalentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TechTalentController<T extends TechTalentDTO, S extends Pageable> {

    @Autowired
    private final TechTalentService<T> techTalentService;

    public TechTalentController(TechTalentService<T> techTalentService) {
        this.techTalentService = techTalentService;
    }

    @PostMapping("/register/tech-talent")
    public ResponseEntity<TechTalentDTO> createTechTalentUser(@RequestBody T techTalentDTO) throws Exception {
        try {
            TechTalentDTO user = techTalentService.createTechTalent(techTalentDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body( null);
        }
    }


    @GetMapping("/users/tech-talent")
    public ResponseEntity<Page<TechTalentUser>> getAllTechTalentUsers(T techTalentDTO, S pageable) {
        try {
            Page<TechTalentUser> users = techTalentService.getAllTechTalent(techTalentDTO, pageable);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}