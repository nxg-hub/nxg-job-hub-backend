package core.nxg.service;

import core.nxg.dto.ReactionsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReactionsService {
    ReactionsDto createReactionOnJobPosting(Long jobId, ReactionsDto reactionsDto);

   // ReactionsDto createReactionOnJobPosting(Long jobId, ReactionsDto reactionsDto);

    ReactionsDto createReactionOnComment(Long commentId, ReactionsDto reactionsDto);

    //List<ReactionsDto> getReactionsForComment(Long commentId);

    //List<ReactionsDto> getReactionsForJobPosting(Long jobId);

    ReactionsDto createReactions(ReactionsDto reactionsDto);

    //List<ReactionsDto> getAllReactions();

    @Transactional
    void deleteReaction(Long reactionId);
}
