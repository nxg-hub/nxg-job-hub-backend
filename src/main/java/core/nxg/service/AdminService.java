package core.nxg.service;

import core.nxg.dto.*;
import core.nxg.entity.EmployerApprovalHistory;
import core.nxg.entity.JobPosting;
import core.nxg.entity.TechTalentApprovalHistory;
import core.nxg.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService  {
    Object getTransactionById(String transactionId, HttpServletRequest request);


    Object getAllTransactions(Pageable pageable, HttpServletRequest request);
    Object getSubscriptions(Pageable pageable, HttpServletRequest request);

    Page<UserResponseDto> getAllUsers(Pageable pageable) ;


    Object getAllJobs(Pageable pageable, HttpServletRequest request);

    void acceptJob(String jobId, HttpServletRequest request);

    void rejectJob(String jobId, String disapprovalReason, HttpServletRequest request);

    void suspendUser(String userId , String reasonForProfileSuspension, HttpServletRequest request);

     void suspendJob(String jobId,String suspensionReason, HttpServletRequest request);


    Page<User> getTalentUsers(int page, int size, HttpServletRequest request);

    Page<User> getAgentUsers(int page, int size, HttpServletRequest request);

    Page<User> getEmployerUsers(int page, int size, HttpServletRequest request);


     Object createAdmin(UserDTO userDTO, HttpServletRequest request) throws Exception ;


    JobPosting AdminCreatejobPosting(AdminJobPostingDto jobPostingDto);

    Object login(LoginDTO userDTO, HttpServletRequest request) throws Exception ;

    void rejectEmployerVerification(String employerID, String reasonForRejection, HttpServletRequest request) throws RuntimeException;

    void rejectTechTalentVerification(String techID, String reasonForRejection, HttpServletRequest request) throws RuntimeException;

    Page<TechTalentApprovalHistory> getTechTalentApprovalHistory(int page, int size, HttpServletRequest request);

    Page<EmployerApprovalHistory> getEmployerApprovalHistory(int page, int size, HttpServletRequest request);
}
