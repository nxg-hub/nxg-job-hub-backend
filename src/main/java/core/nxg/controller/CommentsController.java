package core.nxg.controller;

import core.nxg.dto.CommentsDto;
import core.nxg.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentsController {

    private final CommentsService commentsService;

    @GetMapping
    public ResponseEntity<List<CommentsDto>> getAllComments() {
        List<CommentsDto> comments = commentsService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comments-by-{jobID}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsByJobID(@PathVariable String jobID) {
        List<CommentsDto> comments = commentsService.getAllCommentsByJobID(jobID);
        return ResponseEntity.ok(comments);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<CommentsDto> getCommentById(@PathVariable Long id) {
//        CommentsDto comment = commentsService.getCommentById(id);
//        return ResponseEntity.ok(comment);
//    }

    @PostMapping("/create")
    public ResponseEntity<CommentsDto> createComment(@RequestBody CommentsDto commentsDto) {
        CommentsDto createdComment = commentsService.createComments(commentsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommentsDto> updateComment(@PathVariable Long id, @RequestBody CommentsDto commentsDto) {
        CommentsDto updatedComment = commentsService.updateComments(commentsDto, id);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentsService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }
}