package core.nxg.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import core.nxg.dto.NotificationDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.*;
import core.nxg.enums.NotificationType;
import core.nxg.enums.SenderType;
import core.nxg.repository.*;
import core.nxg.service.PushNotifications;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private PushNotifications notificationService;
    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private JobApplicationHistoryRepository jobApplicationHistoryRepository;


    @Override
    public void saveJob(HttpServletRequest request, String jobPostingId) throws Exception{
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
        savedJobRepo.save(newSavedJob);
    }


    @Override
    public void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception {

        final String SYSTEM_PROCESS_ID = "SYSTEM_PROCESS";


        User user = helper.extractLoggedInUser(request);
        
        
        Optional<TechTalentDTO> techTalentUser = techRepo.findByUser(user);
        if (techTalentUser.isEmpty()){
            throw new UserNotFoundException("Type of user cannot apply to jobs!");
        }

        Optional<JobPosting> job = jobRepo.findById(applicationDTO.getJobPostingId());


        if (job.isEmpty()){
             throw new NotFoundException("Job posting with jobID cannot be found");
        }


        if (!techTalentUser.get().isVerified()) {
            throw new RuntimeException("Tech Talent is not verified. Please contact admin.");
        }
     
            
        
        Application newApplication = new Application();

        newApplication.setJobPosting(job.get());
        newApplication.setApplicationStatus(ApplicationStatus.PENDING);
        newApplication.setApplicant(user);
        newApplication.setTimestamp(LocalDateTime.now());


        var employeR = employerRepository.findById(job.get().getEmployerID());

        NotificationDTO notificationDTO = new NotificationDTO();
        if (employeR.isPresent()) {

            notificationDTO.setReferencedUserID(
                    employeR
                            .map(employer -> employer.getUser().getId()).orElse(SYSTEM_PROCESS_ID));


            notificationDTO.setSenderID(SYSTEM_PROCESS_ID);
            notificationDTO.setNotificationType(NotificationType.JOB_APPLICATION);
            notificationDTO.setMessage(
                    user.getFirstName() + " "
                            + user.getLastName() +
                            " has applied to your job posting: " + job.get().getJob_title());


            notificationService.pushNotification(notificationDTO, SenderType.SYSTEM_PROCESS);

            emailService.sendEmailAfterApplied(employeR.get().getEmail(), user.getEmail(), job.get() );

            appRepo.save(newApplication);
        }
            

    }

    @Override
    public void acceptApplication(String applicationId, HttpServletRequest request) throws Exception {
        // Find the application by ID
        Optional<Application> applicationOpt = appRepo.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            throw new NotFoundException("Application not found!");
        }
        Application application = applicationOpt.get();

        // Check if the user is authorized to accept the application (employer or admin)
        User user = helper.extractLoggedInUser(request);
        JobPosting jobPosting = application.getJobPosting();
        if (!user.getRoles().getAuthority().equals("ADMIN") || !jobPosting.getEmployer().getUser().equals(user)) {
            throw new Exception("You are not authorized to accept this application.");
        }

        // Update the application status
        application.setApplicationStatus(ApplicationStatus.APPROVED);
        appRepo.save(application);

        JobApplicationHistory jobApplicationHistory = new JobApplicationHistory();
        jobApplicationHistory.setJobId(application.getJobPosting().getJobID());
        jobApplicationHistory.setEmployerId(application.getJobPosting().getEmployerID());
        jobApplicationHistory.setTechTalentId(application.getApplicant().getId());
        jobApplicationHistory.setTechTalentName(application.getApplicant().getName());
        jobApplicationHistory.setTimestamp(LocalDateTime.now());
        jobApplicationHistory.setAdminId(user.getId());
        jobApplicationHistory.setEmployerName(application.getJobPosting().getEmployer_name());
        jobApplicationHistory.setApprovalStatus(ApplicationStatus.APPROVED);
        jobApplicationHistoryRepository.save(jobApplicationHistory);

        // Send congratulatory email to the tech talent
        String techTalentEmail = application.getApplicant().getEmail();
        String employerEmail = jobPosting.getEmployer().getEmail();
        // Retrieve all admin emails
        List<String> adminEmails = userRepo.findByRolesAuthority("ADMIN")
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        emailService.sendAcceptanceEmail(techTalentEmail, employerEmail, adminEmails, jobPosting);

        // Notify the admin about the acceptance
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setReferencedUserID(user.getId());
        notificationDTO.setSenderID(user.getId());
        notificationDTO.setNotificationType(NotificationType.APPLICATION_ACCEPTED);
        notificationDTO.setMessage("Tech Talent " + application.getApplicant().getFirstName() + " " +
                application.getApplicant().getLastName() + " has been accepted for the job: " +
                jobPosting.getJob_title());
        notificationService.pushNotification(notificationDTO, SenderType.SYSTEM_PROCESS);
    }

    @Override
    public void rejectApplication(String applicationId, HttpServletRequest request) throws Exception {
        // Find the application by ID
        Optional<Application> applicationOpt = appRepo.findById(applicationId);
        if (applicationOpt.isEmpty()) {
            throw new NotFoundException("Application not found!");
        }
        Application application = applicationOpt.get();

        // Check if the user is authorized to reject the application (employer or admin)
        User user = helper.extractLoggedInUser(request);
        JobPosting jobPosting = application.getJobPosting();
        if (!user.getRoles().getAuthority().equals("ADMIN") || !jobPosting.getEmployer().getUser().equals(user))  {
            throw new Exception("You are not authorized to reject this application.");
        }

        // Update the application status
        application.setApplicationStatus(ApplicationStatus.REJECTED);
        appRepo.save(application);

        JobApplicationHistory jobApplicationHistory = new JobApplicationHistory();
        jobApplicationHistory.setJobId(application.getJobPosting().getJobID());
        jobApplicationHistory.setEmployerId(application.getJobPosting().getEmployerID());
        jobApplicationHistory.setTechTalentId(application.getApplicant().getId());
        jobApplicationHistory.setTechTalentName(application.getApplicant().getName());
        jobApplicationHistory.setTimestamp(LocalDateTime.now());
        jobApplicationHistory.setAdminId(user.getId());
        jobApplicationHistory.setEmployerName(application.getJobPosting().getEmployer_name());
        jobApplicationHistory.setApprovalStatus(ApplicationStatus.REJECTED);
        jobApplicationHistoryRepository.save(jobApplicationHistory);

        // Send rejection email to the tech talent
        String techTalentEmail = application.getApplicant().getEmail();
        // Retrieve all admin emails
        List<String> adminEmails = userRepo.findByRolesAuthority("ADMIN")
                .stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        emailService.sendRejectionEmail(techTalentEmail,adminEmails, jobPosting);
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

