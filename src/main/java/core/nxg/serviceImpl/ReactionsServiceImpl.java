package core.nxg.serviceImpl;

import core.nxg.dto.ReactionsDto;
import core.nxg.entity.*;
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
        JobPosting jobPosting = jobPostingRepository.findById(String.valueOf(jobID))
                .orElseThrow(() -> new NotFoundException("Job posting with ID " + jobID + " not found"));

        Reactions reactions = new Reactions();
        reactions.setReactionType(reactionsDto.getReactionType());
        jobPosting.getReactions().add(reactions);
        jobPostingRepository.save(jobPosting);

        return mapToDto(reactionsRepository.save(reactions));
    }



    @Override
    public ReactionsDto createReactionOnComment(Long commentID, ReactionsDto reactionsDto) {
        Comments comment = commentsRepository.findById(String.valueOf(commentID))
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
        Comments comment = commentsRepository.findById(String.valueOf(commentID))
                .orElseThrow(() -> new NotFoundException("Comment with ID " + commentID + " not found"));

        return comment.getReactions()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<ReactionsDto> getReactionsForJobPosting(Long jobID) {
        JobPosting jobPosting = jobPostingRepository.findById(String.valueOf(jobID))
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


    private ReactionsDto mapToDto(Reactions reactions) {
        ReactionsDto reactionsDto = new ReactionsDto();
        BeanUtils.copyProperties(reactions, reactionsDto);
        return reactionsDto;
    }

}

