package core.nxg.service;

import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    Object getTransactionById(Long transactionId);


    Object getAllTransactions(Pageable pageable);

    Page<UserResponseDto> getAllUsers(Pageable pageable) ;


    Object getAllJobs(Pageable pageable);

    void acceptJob(Long jobId);

    void rejectJob(Long jobId);

    void suspendUser(Long userId);

     void suspendJob(Long jobId);


    Page<User> getTalentUsers(int page, int size);

    Page<User> getAgentUsers(int page, int size);

    Page<User> getEmployerUsers(int page, int size);


}
