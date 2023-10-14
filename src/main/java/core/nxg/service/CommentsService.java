package core.nxg.service;

import core.nxg.dto.CommentsDto;
import core.nxg.entity.Comments;

import java.util.List;

public interface CommentsService {
    Comments createComments(CommentsDto commentsDto);

    List<CommentsDto> getAllComments();

    List<CommentsDto> getAllCommentsByJobID(Long jobID);

    CommentsDto updateComments(CommentsDto commentsDto, Long id);

    CommentsDto getCommentById(Long id);

    void deleteCommentById(Long id);
}