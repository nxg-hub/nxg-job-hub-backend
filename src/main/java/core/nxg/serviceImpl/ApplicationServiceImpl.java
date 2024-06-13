package core.nxg.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import core.nxg.dto.TechTalentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import core.nxg.dto.ApplicationDTO;
import core.nxg.enums.ApplicationStatus;
import core.nxg.entity.Application;
import core.nxg.entity.JobPosting;
import core.nxg.entity.SavedJobs;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.ApplicationRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.SavedJobRepository;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.ApplicationService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class ApplicationServiceImpl implements ApplicationService {


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
        User user = helper.extractLoggedInUser(request);
        
        
        Optional<TechTalentDTO> techTalentUser = techRepo.findByUser(user);
        if (techTalentUser.isEmpty()){
            throw new UserNotFoundException("Applicant cannot be found!");
        }

        Optional<JobPosting> job = jobRepo.findById(applicationDTO.getJobPostingId());
        if (job.isEmpty()){
             throw new NotFoundException("Cannot Apply to an Non-existing Job");}
     
            
        
        Application newApplication = new Application();

        newApplication.setJobPosting(job.get());
        newApplication.setApplicationStatus(ApplicationStatus.PENDING);
        newApplication.setApplicant(user);
        newApplication.setTimestamp(LocalDateTime.now());

        appRepo.saveAndFlush(newApplication);
            

    }

    @Override
    public void updateApplication(ApplicationDTO applicationDTO){

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

    // todo: apply to a job posting. and return an email notification to the employer.
    // todo:
        


        
 

}

