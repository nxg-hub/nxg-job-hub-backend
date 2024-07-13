package core.nxg.serviceImpl;


import core.nxg.configs.JwtService;
import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.*;
import core.nxg.enums.ApprovalType;
import core.nxg.enums.Roles;
import core.nxg.enums.UserType;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.*;
import core.nxg.service.AdminService;
import core.nxg.subscription.enums.JobStatus;
import core.nxg.subscription.repository.SubscribeRepository;
import core.nxg.subscription.repository.TransactionRepository;
import core.nxg.utils.Helper;
import core.nxg.utils.SecretService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    @Autowired
    private final SecretService secretService;
    @Autowired
    private final Helper<?,?> helper;
    @Autowired
    private final SubscribeRepository subRepo;
    @Autowired
    private final TransactionRepository transactionRepository;
    @Autowired
    private final JobPostingRepository jobPostingRepository;
    @Autowired
    private final ModelMapper modelMapper;
    @Autowired
    private final TechTalentServiceImpl<?> techTalentService;
    @Autowired
    private final EmployerServiceImpl employerService;
    @Autowired
    private final UserRepository userRepository;

    private final EmployerRepository employerRepository;

    private final JwtService jwt;

    private final EmployerApprovalHistoryRepository employerApprovalHistoryRepository;

    private final TechTalentApprovalHistoryRepository techTalentApprovalHistoryRepository;

    private static final String INVALID_HEADER_RESPONSE = "Header is Empty or Invalid. Please retry with a valid one or contact the support ";
    @Autowired
    private TechTalentRepository techTalentRepository;

    @Override
    public Object getAllTransactions(Pageable pageable, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return transactionRepository.findAll(pageable);

        }
        return INVALID_HEADER_RESPONSE;
    }

    public Object getTransactionById(String transactionId, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return transactionRepository.findById(String.valueOf(transactionId))
                    .orElseThrow(() -> new NoSuchElementException("Transaction with ID not found"));

        }
        return INVALID_HEADER_RESPONSE;
    }

    @Override
    public Object getAllJobs(Pageable pageable, HttpServletRequest request) {
        if (validateAdminRequest(request)) {


            return jobPostingRepository.findAll(pageable);
        }
        return INVALID_HEADER_RESPONSE;
    }

    @Override
    public void acceptJob(String jobId, HttpServletRequest request) {



        if (validateAdminRequest(request)) {

            String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
            var job = jobPostingRepository.findById(String.valueOf(jobId))
                    .orElseThrow(() -> new NoSuchElementException("Job with ID not found"));
            job.setJobStatus(JobStatus.ACCEPTED);
            job.setActive(true);
            jobPostingRepository.save(job);

            EmployerApprovalHistory employerApprovalHistory = new EmployerApprovalHistory();
            employerApprovalHistory.setEmployerId(job.getEmployerID());
            employerApprovalHistory.setApprovalType(ApprovalType.JOB_POSTING);
            employerApprovalHistory.setApprovalOfficerName(loggedInUser);
            employerApprovalHistory.setJobId(job.getJobID());
            employerApprovalHistory.setJobTitle(job.getJob_title());
            employerApprovalHistory.setDateOfApproval(LocalDateTime.now());
            employerApprovalHistory.setUserType(UserType.EMPLOYER);
            employerApprovalHistoryRepository.save(employerApprovalHistory);
        }else {
            throw new SecurityException("Unauthorized request");
        }
    }

    @Override
    public void rejectJob(String jobId, String disapprovalReason, HttpServletRequest request) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();

        if (validateAdminRequest(request)) {
            var job = jobPostingRepository.findById(String.valueOf(jobId))
                    .orElseThrow(() -> new NoSuchElementException("Job with ID not found"));
            job.setJobStatus(JobStatus.REJECTED);
            job.setActive(false);
            jobPostingRepository.save(job);

            EmployerApprovalHistory employerApprovalHistory = new EmployerApprovalHistory();
            employerApprovalHistory.setEmployerId(job.getEmployerID());
            employerApprovalHistory.setApprovalType(ApprovalType.JOB_POSTING);
            employerApprovalHistory.setApprovalOfficerName(loggedInUser);
            employerApprovalHistory.setJobId(job.getJobID());
            employerApprovalHistory.setJobTitle(job.getJob_title());
            employerApprovalHistory.setDateOfDisapproval(LocalDateTime.now());
            employerApprovalHistory.setDisapprovalReason(employerApprovalHistory.getDisapprovalReason());
            employerApprovalHistory.setUserType(UserType.EMPLOYER);
            employerApprovalHistoryRepository.save(employerApprovalHistory);
        }else {
            throw new SecurityException("Unauthorized request");
        }
    }


    @Override
    public void suspendJob(String jobId,String suspensionReason, HttpServletRequest request) {

        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (validateAdminRequest(request)) {
            var job = jobPostingRepository.findById(String.valueOf(jobId))
                    .orElseThrow(() -> new NoSuchElementException("Job with ID not found"));
            job.setJobStatus(JobStatus.SUSPENDED);
            job.setActive(false);
            jobPostingRepository.save(job);

            EmployerApprovalHistory employerApprovalHistory = new EmployerApprovalHistory();
            employerApprovalHistory.setEmployerId(job.getEmployerID());
            employerApprovalHistory.setApprovalType(ApprovalType.JOB_SUSPENSION);
            employerApprovalHistory.setApprovalOfficerName(loggedInUser);
            employerApprovalHistory.setJobId(job.getJobID());
            employerApprovalHistory.setJobTitle(job.getJob_title());
            employerApprovalHistory.setDateOfJobSuspension(LocalDateTime.now());
            employerApprovalHistory.setReasonForJobSuspension(employerApprovalHistory.getReasonForJobSuspension());
            employerApprovalHistory.setUserType(UserType.EMPLOYER);
            employerApprovalHistoryRepository.save(employerApprovalHistory);
        } else {
            throw new SecurityException("Unauthorized request");


        }
    }


    @Override
    public void suspendUser(String userId, String reasonForProfileSuspension, HttpServletRequest request) {

        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (validateAdminRequest(request)) {
            var user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User with ID not found"));
            user.setEnabled(false);
            userRepository.save(user);

            if (user.getUserType() == UserType.TECHTALENT) {
                TechTalentUser techTalentUser = user.getTechTalent();
                TechTalentApprovalHistory techTalentApprovalHistory = new TechTalentApprovalHistory();
                techTalentApprovalHistory.setTechTalentId(techTalentUser.getTechId());
                techTalentApprovalHistory.setApprovalType(ApprovalType.PROFILE_SUSPENSION);
                techTalentApprovalHistory.setApprovalOfficerName(loggedInUser);
                techTalentApprovalHistory.setTechTalentName(techTalentUser.getUser().getName());
                techTalentApprovalHistory.setDateOfProfileSuspension(LocalDateTime.now());
                techTalentApprovalHistory.setReasonForProfileSuspension(reasonForProfileSuspension);
                techTalentApprovalHistory.setUserType(UserType.TECHTALENT);
                techTalentApprovalHistoryRepository.save(techTalentApprovalHistory);
                
            }
            else {
                if (user.getUserType() == UserType.EMPLOYER) {
                    Employer employer = user.getEmployer();
                    EmployerApprovalHistory employerApprovalHistory = new EmployerApprovalHistory();
                    employerApprovalHistory.setEmployerId(employer.getEmployerID());
                    employerApprovalHistory.setApprovalType(ApprovalType.PROFILE_SUSPENSION);
                    employerApprovalHistory.setApprovalOfficerName(loggedInUser);
                    employerApprovalHistory.setEmployerName(employer.getUser().getEmployer().getCompanyName());
                    employerApprovalHistory.setDateOfProfileSuspension(LocalDateTime.now());
                    employerApprovalHistory.setReasonForProfileSuspension(employerApprovalHistory.getReasonForJobSuspension());
                    employerApprovalHistory.setUserType(UserType.EMPLOYER);
                    employerApprovalHistoryRepository.save(employerApprovalHistory);

                }
            }


        }else {
            throw new SecurityException("Unauthorized request");
        }
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
    public Page<User> getTalentUsers(int page, int size, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return getUsersByType(UserType.TECHTALENT, page, size);
        }
        return Page.empty();
    }

    @Override
    public Page<User> getAgentUsers(int page, int size, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return getUsersByType(UserType.AGENT, page, size);
        }
        return Page.empty();
    }


    @Override
    public Page<User> getEmployerUsers(int page, int size, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return getUsersByType(UserType.EMPLOYER, page, size);
        }
        return Page.empty();
    }


    @Override
    public Object createAdmin(UserDTO userDto, HttpServletRequest request) throws Exception {


        if (validateAdminRequest(request)) {


            userRepository.findByEmail(userDto.getEmail()).ifPresentOrElse(
                    existingAdmin -> {


                        existingAdmin.setFirstName(userDto.getFirstName());
                        existingAdmin.setLastName(userDto.getLastName());
                        existingAdmin.setPhoneNumber(userDto.getPhoneNumber());

                    },
                    () -> {
                        User admin = new User();

                        admin.setEmail(userDto.getEmail());
                        admin.setFirstName(userDto.getFirstName());
                        admin.setLastName(userDto.getLastName());
                        admin.setPhoneNumber(userDto.getPhoneNumber());

                        admin.setPassword(helper.encodePassword(userDto.getPassword()));
                        admin.setRoles(Roles.ADMIN);
                        admin.setEnabled(true);

                        userRepository.save(admin);

                    }

            );

            return userDto;
        }
        return INVALID_HEADER_RESPONSE;
    }

//
//    @Override
//    public Object login(LoginDTO dto, HttpServletRequest request) {
//        var userEmail = userRepository.findByEmail(dto.getEmail());
//
//        if (validateAdminRequest(request)) {
//
//            var user = userRepository.findByEmail(dto.getEmail())
//                    .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));
//
//            if (!helper.encoder.matches(dto.getPassword(), user.getPassword())) {
//                log.error("Invalid Password!");
//                throw new RuntimeException("Invalid email or password!");
//            }
//
//            if (isAdmin(user)) {
////                return user;
//                // Generate JWT Token
//                String token = jwt.generateToken(userEmail.get());
//                return token; // Return the token
//            } else {
//                log.error("User is not an admin");
//                throw new RuntimeException("User is not an admin");
//            }
//
//
//        }
//        return INVALID_HEADER_RESPONSE;
//
//
//    }

    @Override
    public String login(LoginDTO dto, HttpServletRequest request) {

        // Check if the request contains a valid header
        if (validateAdminRequest(request)) {

            // Retrieve the user from the repository based on email
            var user = userRepository.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

            // Validate the password
            if (!helper.encoder.matches(dto.getPassword(), user.getPassword())) {
                log.error("Invalid Password!");
                throw new RuntimeException("Invalid email or password!");
            }

            // Check if the user is an admin
            if (isAdmin(user)) {
                // Generate and return JWT Token
                try {
                    String token = jwt.generateToken(user); // Ensure your jwt.generateToken method accepts a User object
                    return token;
                } catch (Exception e) {
                    log.error("Token generation failed", e);
                    throw new RuntimeException("Token generation failed");
                }
            } else {
                log.error("User is not an admin");
                throw new RuntimeException("User is not an admin");
            }

        }

        return INVALID_HEADER_RESPONSE;
    }


    protected boolean isAdmin(User user){
        return (Roles.ADMIN).equals(user.getRoles());
    }

    protected boolean validateAdminRequest(HttpServletRequest request) {

        return secretService.decodeKeyFromHeaderAndValidate(request);

    }


    @Override
    public Object getSubscriptions(Pageable pageable, HttpServletRequest request){

        if (validateAdminRequest(request)) {
            return subRepo.findAll(pageable);

        }
        return INVALID_HEADER_RESPONSE;
    }
    
    public void verifyTechTalent(String techID){

        techTalentService.verifyTechTalent(techID);
    }
    
    public void verifyEmployer(String employerID){
        
         employerService.verifyEmployer((employerID));
    }

    @Override
    public void rejectEmployerVerification(String employerID, String reasonForRejection, HttpServletRequest request) throws RuntimeException {
        String loggedInUser = request.getUserPrincipal().getName(); // Get logged in user from request

        employerRepository.findById(employerID).ifPresent(employer -> {
            employer.setVerified(false); // Assuming this sets verification status to false
            employer.setEmployerApprovingOfficer(loggedInUser);
            employer.setEmployerDateOfDisApproval(LocalDateTime.now());
            employerRepository.save(employer);

            EmployerApprovalHistory employerApprovalHistory = new EmployerApprovalHistory();
            employerApprovalHistory.setEmployerId(employer.getEmployerID());
            employerApprovalHistory.setApprovalType(ApprovalType.PROFILE_REJECTION);
            employerApprovalHistory.setApprovalOfficerName(loggedInUser);
            employerApprovalHistory.setEmployerName(employer.getUser().getName());
            employerApprovalHistory.setDateOfApproval(LocalDateTime.now());
            employerApprovalHistory.setProfileVerificationRejectionReason(reasonForRejection); // Set rejection reason
            employerApprovalHistory.setUserType(UserType.EMPLOYER);
            employerApprovalHistoryRepository.save(employerApprovalHistory);
        });
    }

    @Override
    public void rejectTechTalentVerification(String techID, String reasonForRejection, HttpServletRequest request) throws RuntimeException {
        String loggedInUser = request.getUserPrincipal().getName(); // Get logged in user from request

        techTalentRepository.findById(techID).ifPresent(techTalent -> {
            techTalent.setVerified(false);
            techTalent.setTechTalentApprovingOfficer(loggedInUser);
            techTalent.setTechTalentDateOfApproval(LocalDateTime.now());
            techTalentRepository.save(techTalent);

            TechTalentApprovalHistory techTalentApprovalHistory = new TechTalentApprovalHistory();
            techTalent.setTechId(techTalent.getTechId());
            techTalentApprovalHistory.setApprovalType(ApprovalType.PROFILE_REJECTION);
            techTalentApprovalHistory.setApprovalOfficerName(loggedInUser);
            techTalentApprovalHistory.setTechTalentName(techTalent.getUser().getName());
            techTalentApprovalHistory.setDateOfApproval(LocalDateTime.now());
            techTalentApprovalHistory.setProfileVerificationRejectionReason(reasonForRejection); // Set rejection reason
            techTalentApprovalHistory.setUserType(UserType.TECHTALENT);
            techTalentApprovalHistoryRepository.save(techTalentApprovalHistory);
        });
    }



    @Override
    public Page<TechTalentApprovalHistory> getTechTalentApprovalHistory(int page, int size, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return techTalentApprovalHistoryRepository.findAll(PageRequest.of(page, size));
        }
        return Page.empty();
    }

    @Override
    public Page<EmployerApprovalHistory> getEmployerApprovalHistory(int page, int size, HttpServletRequest request) {
        if (validateAdminRequest(request)) {
            return employerApprovalHistoryRepository.findAll(PageRequest.of(page, size));
        }
        return Page.empty();
    }

}


