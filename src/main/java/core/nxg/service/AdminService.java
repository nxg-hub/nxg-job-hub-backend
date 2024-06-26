package core.nxg.service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService  {
    Object getTransactionById(Long transactionId);


    Object getAllTransactions(Pageable pageable);
    Object getSubscriptions(Pageable pageable);

    Page<UserResponseDto> getAllUsers(Pageable pageable) ;


    Object getAllJobs(Pageable pageable);

    void acceptJob(Long jobId);

    void rejectJob(Long jobId);

    void suspendUser(Long userId);

     void suspendJob(Long jobId);


    Page<User> getTalentUsers(int page, int size);

    Page<User> getAgentUsers(int page, int size);

    Page<User> getEmployerUsers(int page, int size);


     Object createAdmin(UserDTO userDTO, HttpServletRequest request) throws Exception ;


     Object login(LoginDTO userDTO, HttpServletRequest request) throws Exception ;

}
