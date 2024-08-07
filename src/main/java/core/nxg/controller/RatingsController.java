package core.nxg.controller;

import core.nxg.dto.RatingsDto;
import core.nxg.service.RatingsService;
import core.nxg.serviceImpl.RatingsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    private final RatingsServiceImpl ratingsService;
    private final RatingsServiceImpl ratingsServiceImpl;

    public RatingsController(RatingsServiceImpl ratingsService, RatingsServiceImpl ratingsServiceImpl) {
        this.ratingsService = ratingsService;
        this.ratingsServiceImpl = ratingsServiceImpl;
    }

//    @PostMapping("/create")
//    public RatingsDto createRatings(@RequestBody RatingsDto ratingsDto) {
//       return ratingsService.createRatings(ratingsDto);
//
//    }
@PostMapping("/create")
public ResponseEntity<RatingsDto> createRatings( @RequestBody RatingsDto ratingsDto) {
    RatingsDto createdRatings = ratingsService.createRatings(ratingsDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdRatings);
}



    @GetMapping("/all-{Id}")
    public ResponseEntity<List<RatingsDto>> getAllRatings(@PathVariable String Id) {
        List<RatingsDto> ratings = ratingsService.getRatingsForEmployer(Id);
        return ResponseEntity.ok(ratings);
    }




    @DeleteMapping("/{ratingsId}")
    public ResponseEntity<Void> deleteRatings(@PathVariable String ratingsId) {
        ratingsService.deleteRatings(ratingsId);
        return ResponseEntity.noContent().build();
    }
}
