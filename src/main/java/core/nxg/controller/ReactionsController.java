package core.nxg.controller;

import core.nxg.dto.ReactionsDto;
import core.nxg.service.ReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class ReactionsController {

    private final ReactionsService reactionsService;

    @PostMapping("/create")
    public ResponseEntity<ReactionsDto> createReaction(@RequestBody ReactionsDto reactionsDto) {
        ReactionsDto createdReaction = reactionsService.createReactions(reactionsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReaction);
    }
    @PostMapping("/create/on-jobPosting/{jobID}")
    public ResponseEntity<ReactionsDto> createReactionOnJobPosting(
            @PathVariable Long jobID, @RequestBody ReactionsDto reactionsDto) {
        ReactionsDto createdReaction = reactionsService.createReactionOnJobPosting(jobID, reactionsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReaction);
    }

    @PostMapping("/create/on-comment/{commentID}")
    public ResponseEntity<ReactionsDto> createReactionOnComment(
            @PathVariable Long commentID, @RequestBody ReactionsDto reactionsDto) {
        ReactionsDto createdReaction = reactionsService.createReactionOnComment(commentID, reactionsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReaction);
    }
    @GetMapping("/all")
    public ResponseEntity<List<ReactionsDto>> getAllReactions() {
        List<ReactionsDto> reactions = reactionsService.getAllReactions();
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/comment/{commentID}")
    public ResponseEntity<List<ReactionsDto>> getReactionsForComment(@PathVariable Long commentID) {
        List<ReactionsDto> reactions = reactionsService.getReactionsForComment(commentID);
        return ResponseEntity.ok(reactions);
    }

    @GetMapping("/job/{jobID}")
    public ResponseEntity<List<ReactionsDto>> getReactionsForJobPosting(@PathVariable Long jobID) {
        List<ReactionsDto> reactions = reactionsService.getReactionsForJobPosting(jobID);
        return ResponseEntity.ok(reactions);
    }

    @DeleteMapping("/delete/{reactionId}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long reactionId) {
        reactionsService.deleteReaction(reactionId);
        return ResponseEntity.noContent().build();
    }


}
