package core.nxg.serviceImpl;

import core.nxg.dto.ReactionsDto;
import core.nxg.entity.Comments;
import core.nxg.entity.JobPosting;
import core.nxg.entity.Reactions;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.CommentsRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.ReactionsRepository;
import core.nxg.service.ReactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReactionsServiceImpl implements ReactionsService {

    private final ReactionsRepository reactionsRepository;
    private final JobPostingRepository jobPostingRepository;
    private final CommentsRepository commentsRepository;

    @Override
    public ReactionsDto createReactionOnJobPosting(Long jobID, ReactionsDto reactionsDto) {
        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobID(jobID)
                .orElseThrow(() -> new NotFoundException("Job posting with ID " + jobID + " not found"));

        Reactions reactions = new Reactions();
        reactions.setReactionType(reactionsDto.getReactionType());
        jobPosting.getReactions().add(reactions);
        jobPostingRepository.save(jobPosting);

        return mapToDto(reactionsRepository.save(reactions));
    }

    @Override
    public ReactionsDto createReactionOnComment(Long commentID, ReactionsDto reactionsDto) {
        Comments comment = commentsRepository.findById(commentID)
                .orElseThrow(() -> new NotFoundException("Comment with ID " + commentID + " not found"));

        Reactions reactions = new Reactions();
        reactions.setReactionType(reactionsDto.getReactionType());

        comment.getReactions().add(reactions);
        commentsRepository.save(comment);

        return this.mapToDto(
                reactionsRepository.save(reactions));
    }

    @Override
    public List<ReactionsDto> getReactionsForComment(Long commentID) {
        Comments comment = commentsRepository.findById(commentID)
                .orElseThrow(() -> new NotFoundException("Comment with ID " + commentID + " not found"));

        return comment.getReactions()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReactionsDto> getReactionsForJobPosting(Long jobID) {
        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobID(jobID)
                .orElseThrow(() -> new NotFoundException("Job posting with ID " + jobID + " not found"));

        return jobPosting.getReactions()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ReactionsDto createReactions(ReactionsDto reactionsDto) {
        Reactions reactions = new Reactions();
        reactions.setReactionType(reactionsDto.getReactionType());
        return this.mapToDto(reactionsRepository.save(reactions));
    }

    @Override
    public List<ReactionsDto> getAllReactions() {
        return reactionsRepository.findAll()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReaction(Long reactionId) {
        Reactions reaction = reactionsRepository.findById(reactionId)
                .orElseThrow(() -> new NotFoundException("Reaction with ID " + reactionId + " not found"));

        for (JobPosting jobPosting : reaction.getJobPosting()) {
            jobPosting.getReactions().remove(reaction);
        }
        for (Comments comment : reaction.getComments()) {
            comment.getReactions().remove(reaction);
        }
        reactionsRepository.delete(reaction);
    }

    private ReactionsDto mapToDto(Reactions reactions) {
        ReactionsDto reactionsDto = new ReactionsDto();
        BeanUtils.copyProperties(reactions, reactionsDto);
        return reactionsDto;
    }

}

