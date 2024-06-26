package core.nxg.serviceImpl;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;

import core.nxg.dto.NotificationDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.*;
import core.nxg.enums.NotificationType;
import core.nxg.enums.SenderType;
import core.nxg.repository.*;
import core.nxg.service.EmailService;
import core.nxg.service.PushNotifications;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import core.nxg.dto.ApplicationDTO;
import core.nxg.enums.ApplicationStatus;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.service.ApplicationService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ApplicationServiceImpl implements ApplicationService  {



    @Autowired
    private ApplicationRepository appRepo;

    @Autowired
    private TechTalentRepository techRepo;

    @Autowired
    private JobPostingRepository jobRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Helper helper;

    @Autowired
    private SavedJobRepository savedJobRepo;

    @Autowired
    private ModelMapper mapper;

    private PushNotifications notificationService;
    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmailServiceImpl emailService;




        @Override
    public void saveJob(HttpServletRequest request, Long jobPostingId) throws Exception{
        User user = helper.extractLoggedInUser(request);
        Optional<JobPosting> job = jobRepo.findById(jobPostingId);
        if (job.isEmpty()){
            throw new NotFoundException("Cannot Save a Non-existing Job");
        }
        Optional<SavedJobs> savedJob = savedJobRepo.findByUserAndJobPosting(user, job.get());
        if (savedJob.isPresent()){
            throw new Exception("Job Already Saved!");
        }

        SavedJobs newSavedJob = new SavedJobs();
        newSavedJob.setJobPosting(job.get());
        newSavedJob.setUser(user);
        savedJobRepo.saveAndFlush(newSavedJob);
    }


    @Override
    public void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception {

        final long SYSTEM_PROCESS_ID = 100L;


        User user = helper.extractLoggedInUser(request);
        
        
        Optional<TechTalentDTO> techTalentUser = techRepo.findByUser(user);
        if (techTalentUser.isEmpty()){
            throw new UserNotFoundException("Type of user cannot apply to jobs!");
        }

        Optional<JobPosting> job = jobRepo.findById(applicationDTO.getJobPostingId());


        if (job.isEmpty()){
             throw new NotFoundException("Job posting with jobID cannot be found");
        }
     
            
        
        Application newApplication = new Application();

        newApplication.setJobPosting(job.get());
        newApplication.setApplicationStatus(ApplicationStatus.PENDING);
        newApplication.setApplicant(user);
        newApplication.setTimestamp(LocalDateTime.now());


        var employeR = employerRepository.findById(Long.valueOf(job.get().getEmployerID()));

        NotificationDTO notificationDTO = new NotificationDTO();
        if (employeR.isPresent()) {

            notificationDTO.setReferencedUserID(
                    employeR
                            .map(employer -> employer.getUser().getId()).orElse(null));


            notificationDTO.setSenderID(SYSTEM_PROCESS_ID);
            notificationDTO.setNotificationType(NotificationType.JOB_APPLICATION);
            notificationDTO.setMessage(
                    user.getFirstName() + " "
                            + user.getLastName() +
                            " has applied to your job posting: " + job.get().getJob_title());


            notificationService.pushNotification(notificationDTO, SenderType.SYSTEM_PROCESS);

            emailService.sendEmailAfterApplied(employeR.get().getEmail(), user.getEmail() );

            appRepo.saveAndFlush(newApplication);
        }
            

    }




    

    @Override
    public Page<ApplicationDTO> getMyApplications(HttpServletRequest request, Pageable pageable) throws NullPointerException{
        try{
            User user = helper.extractLoggedInUser(request);   
        
            Page<Application> applications = appRepo.findByApplicant(user, pageable);
            if(applications == null){
                throw new NotFoundException("You do not have any applications at the moment");
            }

            return applications.map(a -> mapper.map(a ,ApplicationDTO.class));

        }
        catch(Exception e){
            return null;}
    }

    @Override
    public Page<SavedJobs> getMySavedJobs(HttpServletRequest request, Pageable pageable) throws Exception{
        User user = helper.extractLoggedInUser(request);
        return savedJobRepo.findByUser(user, pageable);

    }






        
 

}

