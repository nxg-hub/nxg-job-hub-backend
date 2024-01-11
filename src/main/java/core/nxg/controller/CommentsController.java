package core.nxg.controller;

import core.nxg.dto.CommentsDto;
import core.nxg.entity.Comments;
import core.nxg.service.CommentsService;
import core.nxg.serviceImpl.CommentsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentsController {

    @Autowired
    private final CommentsServiceImpl commentsService;

    @PostMapping
    public Comments createComments(@RequestBody CommentsDto commentsDto) {
        return commentsService.createComments(commentsDto);
    }

//    @GetMapping
//    public List<CommentsDto> getAllComments() {
//        return commentsService.getAllComments();
//    }

//    @GetMapping("/{id}")
//    public CommentsDto getCommentById(@PathVariable Long id) {
//        return commentsService.getCommentById(id);
//    }

    @GetMapping("/job/{jobId}")
    public List<CommentsDto> getAllCommentsByJobId(@PathVariable Long jobId) {
        return commentsService.getAllCommentsByJobId(jobId);
    }

    @PutMapping("/{id}")
    public CommentsDto updateComments(@RequestBody CommentsDto commentsDto, @PathVariable Long id) {
        return commentsService.updateComments(commentsDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCommentById(@PathVariable Long id) {
        commentsService.deleteCommentById(id);
    }

    @GetMapping("/page")
    public Page<CommentsDto> getAllCommentsPage(@RequestParam int page, @RequestParam int size) {
        return commentsService.getAllCommentsPage(page, size);
    }
}
