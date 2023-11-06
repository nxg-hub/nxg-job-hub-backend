package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.service.ApplicationService;
import core.nxg.service.TechTalentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.SavedJobs;
import core.nxg.entity.Skill;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.ApplicationDTO;
import core.nxg.dto.DashboardDTO;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserResponseDto;
import core.nxg.entity.User;
import core.nxg.controller.TechTalentController;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.stereotype.Service;

import core.nxg.exceptions.NotFoundException;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.utils.Helper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechTalentServiceImpl<T extends TechTalentDTO> implements TechTalentService<T> {

    @Autowired
    private final TechTalentRepository techTalentRepository;


    @Autowired
    private final Helper helper;


    @Autowired
    ModelMapper mapper;

    @Autowired
    ApplicationService appService;



    @Override
    public TechTalentDTO createTechTalent(HttpServletRequest request, TechTalentDTO techTalentDto) throws Exception {
        User loggedInUser = helper.extractLoggedInUser(request);


        TechTalentUser existingTechTalentUser = techTalentRepository.findByUser(loggedInUser)
        .orElseThrow(() -> new UserAlreadyExistException("User with Email Already Exists"));


        TechTalentUser techTalentUser = new TechTalentUser(); 



        techTalentUser.setNationality(techTalentDto.getNationality());
        techTalentUser.setSkills(techTalentDto.getSkills());
        techTalentUser.setResidentialAddress(techTalentDto.getResidentialAddress());
        techTalentUser.setJobType(techTalentDto.getJobType());
        techTalentUser.setHighestQualification(techTalentDto.getHighestQualification());
        techTalentUser.setExperienceLevel(techTalentDto.getExperienceLevel());
        techTalentUser.setYearsOfExperience(techTalentDto.getYearsOfExperience());
        techTalentUser.setCountryCode(techTalentDto.getCountryCode());
        techTalentUser.setWorkMode(techTalentDto.getWorkMode());
        techTalentUser.setCity(techTalentDto.getCity());
        techTalentUser.setCurrentJob(techTalentDto.getCurrentJob());
        techTalentUser.setLinkedInUrl(techTalentDto.getLinkedInUrl());
        techTalentUser.setLocation(techTalentDto.getLocation());
        techTalentUser.setState(techTalentDto.getState());
        techTalentUser.setResume(techTalentDto.getResume());
        List<Skill<String>> skills = techTalentDto.getSkills();
        if (skills != null) {
            for (Skill<String> skill : skills) {
                techTalentUser.addSkill(skill);
            }
        }
        
        techTalentUser.setCoverletter(techTalentDto.getCoverletter());
        techTalentUser.setProfessionalCert(techTalentDto.getProfessionalCert());
        loggedInUser.setTechTalent(techTalentUser);
        techTalentUser.setUser(loggedInUser);
        techTalentRepository.saveAndFlush(techTalentUser);

        return techTalentDto;          


    }
            

    
  
    
    @Override
    public Page<TechTalentDTO> getAllTechTalent(Pageable pageable) throws Exception {
        Page<TechTalentUser> techTalentUser = techTalentRepository.findAll(pageable);
        return techTalentUser.map(TechTalentDTO::new);
       
    }
    @Override
    public TechTalentDTO getTechTalentById(Long Id) throws Exception{
        TechTalentUser user = techTalentRepository.findById(Id)
            .orElseThrow(() -> new NotFoundException("TechTalent Not Found!"));
        return  new TechTalentDTO(user);
    }

    @Override
    public TechTalentDTO updateTechTalent(TechTalentDTO userDto, Long id ) throws Exception {
        TechTalentUser user = techTalentRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setCity(userDto.getCity());
        user.setCountryCode(userDto.getCountryCode());        
        user.setCoverletter(userDto.getCoverletter());
        user.setCurrentJob(userDto.getCurrentJob());
        user.setExperienceLevel(userDto.getExperienceLevel());
        user.setHighestQualification(userDto.getHighestQualification());
        user.setJobType(userDto.getJobType());
        user.setLinkedInUrl(userDto.getLinkedInUrl());
        user.setLocation(userDto.getLocation());
        user.setState(userDto.getState());
        user.setZipCode(userDto.getZipCode());
        user.setYearsOfExperience(userDto.getYearsOfExperience());
        user.setWorkMode(userDto.getWorkMode());
        user.setSkills(userDto.getSkills());
        user.setResume(userDto.getResume());
        user.setProfessionalCert(userDto.getProfessionalCert());
        techTalentRepository.save(user);
        return userDto;


    

}
    @Override
    public void deleteTechTalentUser(Long ID){
        TechTalentUser user = techTalentRepository.findById(ID)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        techTalentRepository.delete(user);
    }


    @Override
    public UserResponseDto getMe(HttpServletRequest request) throws Exception{
        User user = helper.extractLoggedInUser(request);
        return mapper.map(user, UserResponseDto.class);
    }
    @Override
    public DashboardDTO getTechTalentDashboard(HttpServletRequest request, Pageable pageable) throws Exception{
        User user = helper.extractLoggedInUser(request);
        
        List<ApplicationDTO> my_applications = appService.getMyApplications(request, pageable).getContent() ;
        List<SavedJobs> my_saved_jobs = appService.getMySavedJobs(request, pageable).getContent();


        UserResponseDto settings = mapper.map(user, UserResponseDto.class);
        DashboardDTO response = new DashboardDTO();
        response.setMy_applications(my_applications);
        response.setSaved_jobs(my_saved_jobs);
        response.setPrivacy(settings);

        Link selfLink = linkTo(TechTalentController.class)
            .slash("my-dashboard")
            .withSelfRel();

        
        Link myApplicationsLink = linkTo(
        methodOn(TechTalentController.class)
            .getJobApplicationsForUser(request, pageable))
        .withRel("my_applications");

        Link savedJobsLink = linkTo(
            methodOn(TechTalentController.class)
                .getSavedJobs(request, pageable))
            .withRel("saved_jobs");

        Link settingsLink  = linkTo(
            methodOn(TechTalentController.class)
                .profile(request))
            .withRel("settings");

        response.add(settingsLink);
        response.add(myApplicationsLink);
        response.add(savedJobsLink);
        response.add(selfLink);
        return response;
    }
    

}





 



    

