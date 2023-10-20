package core.nxg.serviceImpl;

import core.nxg.dto.CommentsDto;
import core.nxg.entity.Comments;
import core.nxg.entity.JobPosting;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.CommentsRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//TODO use map to dto properly
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final JobPostingRepository jobPostingRepository;

    @Override
    public Comments createComments(CommentsDto commentsDto) {
        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobID(commentsDto.getJobID())
                .orElseThrow(() -> new NotFoundException("Job posting with ID " + commentsDto.getJobID() + " not found"));

        Comments comments = new Comments();
        comments.setComment(commentsDto.getComment());
        comments.setJobPosting(jobPosting);
        return commentsRepository.saveAndFlush(comments);
//        return mapToDto(savedComment);
    }

    @Override
    public List<CommentsDto> getAllComments() {
        List<Comments> comments = commentsRepository.findAll();
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }
    @Override
    public List<CommentsDto> getAllCommentsByJobID(Long jobID) {
        JobPosting jobPosting = jobPostingRepository.findById(jobID).orElseThrow(
                ()-> new NotFoundException("Job Posting not found")
        );

        List<Comments> comments = commentsRepository.findByJobPosting(jobPosting);

        /*TODO Return A Pageable of Comments */

        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }


    // TODO create updateCommentDTO, remove map to DTO, remove jobPosting
    @Override
    public CommentsDto updateComments(CommentsDto commentsDto, Long id) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comments with ID " + id + " not found"));
//        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobID(commentsDto.getJobID())
//                .orElseThrow(() -> new NotFoundException("Job posting with ID " + commentsDto.getJobID() + " not found"));

        comments.setComment(commentsDto.getComment());
//        comments.setJobPosting(jobPosting);
        comments = commentsRepository.save(comments);
        return mapToDto(comments);
    }

    @Override
    public CommentsDto getCommentById(Long id) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comments with ID " + id + " not found"));
        return mapToDto(comments);
    }

    //TODO add a return statement
    @Override
    public void deleteCommentById(Long id) {
        Comments comments = commentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comments with ID " + id + " not found"));
        commentsRepository.delete(comments);
    }

    private CommentsDto mapToDto(Comments comments) {
        CommentsDto commentsDto = new CommentsDto();
        BeanUtils.copyProperties(comments, commentsDto);
        return commentsDto;
    }
}
