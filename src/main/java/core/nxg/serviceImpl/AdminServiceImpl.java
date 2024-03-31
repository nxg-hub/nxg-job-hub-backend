package core.nxg.serviceImpl;


import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.enums.UserType;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.AdminService;
import core.nxg.subscription.enums.JobStatus;
import core.nxg.subscription.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {




    private final TransactionRepository transactionRepository;

    private final JobPostingRepository jobPostingRepository;

    private final ModelMapper modelMapper;


    private final UserRepository userRepository;
    @Override
    public Object getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);

    }

    public Object getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction with ID not found"));

    }

    @Override
    public Object getAllJobs(Pageable pageable) {


        return jobPostingRepository.findAll(pageable);
    }

    @Override
    public void acceptJob(Long jobId) {

        var job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job with ID not found"));
        job.setJobStatus(JobStatus.ACCEPTED);
        job.setActive(true);
        jobPostingRepository.saveAndFlush(job);
    }

    @Override
    public void rejectJob(Long jobId) {

        var job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job with ID not found"));
        job.setJobStatus(JobStatus.REJECTED);
        job.setActive(false);
        jobPostingRepository.save(job);

    }


    @Override
    public void suspendJob(Long jobId) {

        var job = jobPostingRepository.findById(jobId)
                .orElseThrow(() -> new NoSuchElementException("Job with ID not found"));
        job.setJobStatus(JobStatus.SUSPENDED);
        job.setActive(false);
        jobPostingRepository.save(job);

    }



    @Override
    public void suspendUser(Long userId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID not found"));
        user.setEnabled(false);
        userRepository.save(user);



    }
    @Override
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        Page<User> user = userRepository.findAll(pageable);
        return user.map(u -> modelMapper.map(u, UserResponseDto.class));
    }


    public Page<User> getUsersByType(UserType userType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findByUserType(userType, pageable);
    }

    @Override
    public Page<User> getTalentUsers(int page, int size) {
        return getUsersByType(UserType.TECHTALENT, page, size);
    }

    @Override
    public Page<User> getAgentUsers(int page, int size) {
        return getUsersByType(UserType.AGENT, page, size);
    }


    @Override
    public Page<User> getEmployerUsers(int page, int size) {
        return getUsersByType(UserType.EMPLOYER, page, size);
    }

}
