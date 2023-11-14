package core.nxg.service;

import core.nxg.dto.CommentsDto;
import core.nxg.entity.Comments;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentsService {

    Comments createComments(CommentsDto commentsDto);

    List<CommentsDto> getAllCommentsByJobId(Long JobId);

    CommentsDto updateComments(CommentsDto commentsDto, Long Id);

    void deleteCommentById(Long Id);

    Page<CommentsDto> getAllCommentsPage(int page, int size);
}
