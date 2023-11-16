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

    @Override
    public Comments createComments(CommentsDto commentsDto) {
        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobID(commentsDto.getJobID())
                .orElseThrow(() -> new NotFoundException("Job posting with Id " + commentsDto.getJobID() + " not found"));

        Comments comments = new Comments();
        comments.setComment(commentsDto.getComment());
        comments.setJobPosting(jobPosting);
        return commentsRepository.saveAndFlush(comments);
    }


//    @Override
//    public List<CommentsDto> getAllComments() {
//        List<Comments> comments = commentsRepository.findAll();
//        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
//    }

    @Override
    public List<CommentsDto> getAllCommentsByJobId(Long JobId) {
        JobPosting jobPosting = jobPostingRepository.findById(JobId).orElseThrow(
                ()-> new NotFoundException("Job Posting not found")
        );
        List<Comments> comments = commentsRepository.findByJobPosting(jobPosting);
        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CommentsDto updateComments(CommentsDto commentsDto, Long Id) {
        Comments comments = commentsRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("Comments with ID " + Id + " not found"));

        comments.setComment(commentsDto.getComment());
        comments = commentsRepository.save(comments);
        return mapToDto(comments);
    }

//    @Override
//    public CommentsDto getCommentById(Long Id) {
//        Comments comments = commentsRepository.findById(Id)
//                .orElseThrow(() -> new NotFoundException("Comments with ID " + Id + " not found"));
//        return mapToDto(comments);
//    }

    @Override
    public void deleteCommentById(Long Id) {
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





















































//package core.nxg.serviceImpl;
//
//import core.nxg.dto.CommentsDto;
//import core.nxg.entity.Comments;
//import core.nxg.entity.JobPosting;
//import core.nxg.exceptions.NotFoundException;
//import core.nxg.repository.CommentsRepository;
//import core.nxg.repository.JobPostingRepository;
//import core.nxg.service.CommentsService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
////TODO use map to dto properly
//public class CommentsServiceImpl implements CommentsService {
//    private final CommentsRepository commentsRepository;
//    private final JobPostingRepository jobPostingRepository;
//
//    @Override
//    public Comments createComments(CommentsDto commentsDto) {
//        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobId(Long.valueOf(commentsDto.getJobId()))
//                .orElseThrow(() -> new NotFoundException("Job posting with Id " + commentsDto.getJobId() + " not found"));
//
//        Comments comments = new Comments();
//        comments.setComment(commentsDto.getComment());
//        comments.setJobPosting(jobPosting);
//        return commentsRepository.saveAndFlush(comments);
////        return mapToDto(savedComment);
//    }
//
//    @Override
//    public List<CommentsDto> getAllComments() {
//        List<Comments> comments = commentsRepository.findAll();
//        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
//    }
//    @Override
//    public List<CommentsDto> getAllCommentsByJobId(Long jobId) {
//        JobPosting jobPosting = jobPostingRepository.findById(jobId).orElseThrow(
//                ()-> new NotFoundException("Job Posting not found")
//        );
//
//        List<Comments> comments = commentsRepository.findByJobPosting(jobPosting);
//
//        /*TODO Return A Pageable of Comments */
//
//        return comments.stream().map(this::mapToDto).collect(Collectors.toList());
//    }
//
//
//    // TODO create updateCommentDTO, remove map to DTO, remove jobPosting
//    @Override
//    public CommentsDto updateComments(CommentsDto commentsDto, Long id) {
//        Comments comments = commentsRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Comments with ID " + id + " not found"));
////        JobPosting jobPosting = jobPostingRepository.findJobPostingByJobID(commentsDto.getJobID())
////                .orElseThrow(() -> new NotFoundException("Job posting with ID " + commentsDto.getJobID() + " not found"));
//
//        comments.setComment(commentsDto.getComment());
////        comments.setJobPosting(jobPosting);
//        comments = commentsRepository.save(comments);
//        return mapToDto(comments);
//    }
//
//    @Override
//    public CommentsDto getCommentById(Long id) {
//        Comments comments = commentsRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Comments with ID " + id + " not found"));
//        return mapToDto(comments);
//    }
//
//    //TODO add a return statement
//    @Override
//    public void deleteCommentById(Long id) {
//        Comments comments = commentsRepository.findById(id)
//                .orElseThrow(() -> new NotFoundException("Comments with ID " + id + " not found"));
//        commentsRepository.delete(comments);
//    }
//
//    private CommentsDto mapToDto(Comments comments) {
//        CommentsDto commentsDto = new CommentsDto();
//        BeanUtils.copyProperties(comments, commentsDto);
//        return commentsDto;
//    }
//}
