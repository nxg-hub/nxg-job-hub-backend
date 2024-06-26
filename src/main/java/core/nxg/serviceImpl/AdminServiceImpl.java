package core.nxg.serviceImpl;


import core.nxg.dto.LoginDTO;
import core.nxg.dto.UserDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.enums.Roles;
import core.nxg.enums.UserType;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.AdminService;
import core.nxg.service.UserService;
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
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.util.Collections;
import java.util.NoSuchElementException;


@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final SecretService secretService;
    private final Helper helper;
    private final SubscribeRepository subRepo;
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


    @Override
    public Object createAdmin(UserDTO userDto, HttpServletRequest request) throws Exception {



        var header = request.getHeader("x-nxg-header");

        if (header.isEmpty() || header.isBlank()) {
            log.error("**via registration. Header Is Empty" );
            return null;
        }

        if (!secretService.decodeKeyFromHeaderAndValidate(header)) {
            log.error("**via registration. Header Is Invalid");
            throw new RuntimeException("**via registration. Header Is Invalid. Please retry with a valid one or contact the support ");
        }


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

                    userRepository.saveAndFlush(admin);

                }

        );

        return userDto;
    }


    @Override
    public Object login(LoginDTO dto, HttpServletRequest request){


        var nxgHeader = request.getHeader("x-nxg-header");
        if (nxgHeader.isEmpty() || nxgHeader.isBlank()) {
            log.error("Header Is Empty or Invalid");
            return null;
        };

        if (!secretService.decodeKeyFromHeaderAndValidate(nxgHeader)) {
            log.error("***via login . Invalid encoded header. Please retry with a valid one or contact the support");
            throw new RuntimeException("***via login. Header Is Invalid.  Please retry with a valid one or contact the support");
        }

        var user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Invalid email or password"));

        if (!helper.encoder.matches(dto.getPassword(), user.getPassword())) {
            log.error("Invalid Password");
            throw new RuntimeException("Invalid email or password");
        }

        if (isAdmin(user)) {
            return user;
        } else {
            log.error("User is not an admin");
            throw new RuntimeException("User is not an admin");
        }



    }

    protected boolean isAdmin(User user){
        return user.getRoles().equals(Roles.ADMIN);
    }

    public Object getSubscriptions(Pageable pageable){

        return subRepo.findAll(pageable);

    }

}


