package core.nxg.controller;

import core.nxg.dto.RatingsDto;
import core.nxg.entity.Ratings;
import core.nxg.service.RatingsService;
import core.nxg.serviceImpl.RatingsServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    private final RatingsService ratingsService;

    public RatingsController(RatingsServiceImpl ratingsService) {
        this.ratingsService = ratingsService;
    }

    @PostMapping("/create")
    public ResponseEntity<RatingsDto> createRatings(@RequestBody RatingsDto ratingsDto) {
        RatingsDto ratings = ratingsService.createRatings(ratingsDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ratings);
        return new ResponseEntity<>(ratings, HttpStatus.CREATED);

    }

    @GetMapping("/all")
    public ResponseEntity<List<RatingsDto>> getAllRatings() {
        List<RatingsDto> ratings = ratingsService.getAllRatings();
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/all-{employerId}")
    public ResponseEntity<List<RatingsDto>> getAllRatings(@PathVariable Long employerId) {
        List<RatingsDto> ratings = ratingsService.getRatingsForEmployer(employerId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/{ratingsId}")
    public ResponseEntity<RatingsDto> getRatingsById(@PathVariable Long ratingsId) {
        RatingsDto ratings = ratingsService.getRatingsById(ratingsId);
        return ResponseEntity.ok(ratings);
    }

    @PutMapping("/{ratingsId}")
    public ResponseEntity<Void> updateRatings(@PathVariable Long ratingsId, @RequestBody RatingsDto ratingsDto) {
        ratingsService.updateRatings(ratingsId, ratingsDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{ratingsId}")
    public ResponseEntity<Void> deleteRatings(@PathVariable Long ratingsId) {
        ratingsService.deleteRatings(ratingsId);
        return ResponseEntity.noContent().build();
    }
}
