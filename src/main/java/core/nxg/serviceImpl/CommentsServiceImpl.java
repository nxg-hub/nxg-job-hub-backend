package core.nxg.serviceImpl;

import core.nxg.dto.CommentsDto;
import core.nxg.entity.Comments;
import core.nxg.entity.JobPosting;
import core.nxg.entity.Notification;
import core.nxg.entity.User;
import core.nxg.enums.NotificationType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.*;
import core.nxg.service.CommentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsServiceImpl implements CommentsService {
    @Autowired
    private final CommentsRepository commentsRepository;
    @Autowired
    private final JobPostingRepository jobPostingRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final NotificationRepository notificationRepository;

    @Autowired
    private final EmployerRepository employerRepository;

    @Override
    public Comments createComments(CommentsDto commentsDto) { // Assuming Comments are made on job postings only
        JobPosting jobPosting = jobPostingRepository.findById(String.valueOf(commentsDto.getJobID()))
                .orElseThrow(() -> new NotFoundException("Job posting with Id " + commentsDto.getJobID() + " not found"));


        var commenter = userRepository.findById(String.valueOf(commentsDto.getCommenterID()))
                .orElseThrow(() -> new NotFoundException("User with id " + commentsDto.getCommenterID() + " not found"));
        Comments comments = new Comments();
        comments.setComment(commentsDto.getComment());
        comments.setJobPosting(jobPosting);

        var savedcomments = commentsRepository.save(comments);
        notify(savedcomments, jobPosting, commenter );

        return savedcomments;
    }

    private void notify(Comments comment, JobPosting jobPosting, User commenter){

        var employer = employerRepository.findById(jobPosting.getEmployerID())
                .orElseThrow(() -> new NotFoundException("Employer with id " + jobPosting.getEmployerID()+ " not found"));
        var notification = Notification.builder()
                .notificationType(NotificationType.COMMENT)
                .delivered(false)
                .message(comment.getComment())
                .referencedUserID(employer.getUser().getId())
                .senderID(commenter.getId())
                .contentId(comment.getId())
                .build();
        notificationRepository.save(notification);
    }




    @Override
    public List<CommentsDto> getAllCommentsByJobId(String JobId) {
        JobPosting jobPosting = jobPostingRepository.findById(JobId).orElseThrow(
                ()-> new NotFoundException("Job Posting not found")
        );
        List<Comments> comments = commentsRepository.findByJobPosting(jobPosting);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentsDto updateComments(CommentsDto commentsDto, String Id) {
        Comments comments = commentsRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("Comments with ID " + Id + " not found"));

        comments.setComment(commentsDto.getComment());
        comments = commentsRepository.save(comments);
        return mapToDto(comments);
    }


    @Override
    public void deleteCommentById(String Id) {
        Comments comments = commentsRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("Comments with ID " + Id + " not found"));
        commentsRepository.delete(comments);
    }

    @Override
    public Page<CommentsDto> getAllCommentsPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Comments> commentsPage = commentsRepository.findAll(pageable);
        return commentsPage.map(this::mapToDto);
    }
    private CommentsDto mapToDto(Comments comments) {
        CommentsDto commentsDto = new CommentsDto();
        BeanUtils.copyProperties(comments, commentsDto);
        return commentsDto;
    }
}

