package core.nxg.serviceImpl;


import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.AdminService;
import core.nxg.subscription.enums.JobStatus;
import core.nxg.subscription.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final TransactionRepository transactionRepository;

    private final JobPostingRepository jobPostingRepository;

    private final UserRepository userRepository;
    @Override
    public Object getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);

    }

    @Override
    public Object getAllEmployers(Pageable pageable) {

        return null;

    }

    @Override
    public Object getAllJobs(Pageable pageable) {


        return null;
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
}
