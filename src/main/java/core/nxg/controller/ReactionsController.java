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
    @PostMapping("/create/on-jobPosting/{jobId}")
    public ResponseEntity<ReactionsDto> createReactionOnJobPosting(
            @PathVariable String jobId, @RequestBody ReactionsDto reactionsDto) {
        ReactionsDto createdReaction = reactionsService.createReactionOnJobPosting(Long.valueOf(jobId), reactionsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReaction);
    }

    @PostMapping("/create/on-comment/{commentId}")
    public ResponseEntity<ReactionsDto> createReactionOnComment(
            @PathVariable Long commentId, @RequestBody ReactionsDto reactionsDto) {
        ReactionsDto createdReaction = reactionsService.createReactionOnComment(commentId, reactionsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReaction);
    }
//    @GetMapping("/all")
//    public ResponseEntity<List<ReactionsDto>> getAllReactions() {
//        List<ReactionsDto> reactions = reactionsService.getAllReactions();
//        return ResponseEntity.ok(reactions);
//    }

//    @GetMapping("/comment/{commentId}")
//    public ResponseEntity<List<ReactionsDto>> getReactionsForComment(@PathVariable Long commentId) {
//        List<ReactionsDto> reactions = reactionsService.getReactionsForComment(commentId);
//        return ResponseEntity.ok(reactions);
//    }

//    @GetMapping("/job/{jobId}")
//    public ResponseEntity<List<ReactionsDto>> getReactionsForJobPosting(@PathVariable String jobId) {
//        List<ReactionsDto> reactions = reactionsService.getReactionsForJobPosting(Long.valueOf(jobId));
//        return ResponseEntity.ok(reactions);
//    }

    @DeleteMapping("/delete/{reactionId}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long reactionId) {
        reactionsService.deleteReaction(reactionId);
        return ResponseEntity.noContent().build();
    }


}
