package core.nxg.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import core.nxg.dto.ApplicationDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.enums.ApplicationStatus;
import core.nxg.entity.Application;
import core.nxg.entity.JobPosting;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.repository.ApplicationRepository;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.ApplicationService;

@Service
public class ApplicationServiceImpl implements ApplicationService {


    @Autowired
    ApplicationRepository appRepo;

    @Autowired
    TechTalentRepository techRepo;

    @Autowired
    JobPostingRepository jobRepo;

    @Autowired
    UserRepository userRepo;

    @Override
    public void createApplication(ApplicationDTO applicationDTO) throws Exception {
        Optional<User> user = userRepo.findByEmail(applicationDTO.getApplicantEmail());
        if (user.isEmpty()){
            throw new UserNotFoundException("Applicant cannot be found!");}
        
        
        Optional<TechTalentUser> techTalentUser = techRepo.findByUser(user.get());
        if (techTalentUser.isEmpty()){
            throw new UserNotFoundException("Applicant cannot be found!");
        }

        Optional<JobPosting> job = jobRepo.findJobPostingByJobID(applicationDTO.getJobPostingId());
        if (job.isEmpty()){
             throw new NotFoundException("Cannot Apply to an Non-existing Job");}
     
            
        
        Application newApplication = new Application();

        newApplication.setJobPosting(job.get());
        newApplication.setApplicationStatus(ApplicationStatus.PENDING);
        newApplication.setApplicant(techTalentUser.get());
        newApplication.setTimestamp(LocalDateTime.now());

        appRepo.saveAndFlush(newApplication);
            

    };

    @Override
    public void updateApplication(ApplicationDTO applicationDTO){

         };
    

    @Override
    public Page<ApplicationDTO> getMyApplications(User user, Pageable pageable) throws Exception {
        
            TechTalentUser user1 = techRepo.findByUser(user)
        .orElseThrow(
            () -> new UserNotFoundException("TechTalent User does not exist!"))
    ;
        return appRepo.findByApplicant(user, pageable);
 

}
}
