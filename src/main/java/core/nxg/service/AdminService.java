package core.nxg.service;

import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService  {
    Object getTransactionById(Long transactionId, HttpServletRequest request);


    Object getAllTransactions(Pageable pageable, HttpServletRequest request);
    Object getSubscriptions(Pageable pageable, HttpServletRequest request);

    Page<UserResponseDto> getAllUsers(Pageable pageable) ;


    Object getAllJobs(Pageable pageable, HttpServletRequest request);

    void acceptJob(Long jobId, HttpServletRequest request);

    void rejectJob(Long jobId, HttpServletRequest request);

    void suspendUser(Long userId, HttpServletRequest request);

     void suspendJob(Long jobId, HttpServletRequest request);


    Page<User> getTalentUsers(int page, int size, HttpServletRequest request);

    Page<User> getAgentUsers(int page, int size, HttpServletRequest request);

    Page<User> getEmployerUsers(int page, int size, HttpServletRequest request);


     Object createAdmin(UserDTO userDTO, HttpServletRequest request) throws Exception ;


     Object login(LoginDTO userDTO, HttpServletRequest request) throws Exception ;

}
