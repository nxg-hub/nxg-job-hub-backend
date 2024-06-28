package core.nxg.service;

import core.nxg.dto.CommentsDto;
import core.nxg.entity.Comments;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CommentsService {

    Comments createComments(CommentsDto commentsDto);

    List<CommentsDto> getAllCommentsByJobId(String JobId);

    CommentsDto updateComments(CommentsDto commentsDto, String Id);

    void deleteCommentById(String Id);

    Page<CommentsDto> getAllCommentsPage(int page, int size);
}
