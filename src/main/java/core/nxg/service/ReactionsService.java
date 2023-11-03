package core.nxg.service;

import core.nxg.dto.ReactionsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReactionsService {
    ReactionsDto createReactionOnJobPosting(Long jobID, ReactionsDto reactionsDto);

    ReactionsDto createReactionOnComment(Long commentID, ReactionsDto reactionsDto);

    List<ReactionsDto> getReactionsForComment(Long commentID);

    List<ReactionsDto> getReactionsForJobPosting(Long jobID);

    ReactionsDto createReactions(ReactionsDto reactionsDto);

    List<ReactionsDto> getAllReactions();

    @Transactional
    void deleteReaction(Long reactionId);
}
