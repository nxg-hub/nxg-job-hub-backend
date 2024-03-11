package core.nxg.serviceImpl;

import core.nxg.dto.*;
import core.nxg.entity.*;
import core.nxg.enums.UserType;
import core.nxg.exceptions.ExpiredJWTException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.TechTalentAgentRepository;
import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.response.EmployerResponse;
import core.nxg.response.TechTalentResponse;
import core.nxg.service.ApplicationService;
import core.nxg.service.TechTalentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import core.nxg.controller.TechTalentController;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.stereotype.Service;

import core.nxg.exceptions.NotFoundException;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.exceptions.UserNotFoundException;
import core.nxg.utils.Helper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TechTalentServiceImpl<T extends TechTalentDTO> implements TechTalentService<T> {

    @Autowired
    private final TechTalentRepository techTalentRepository;

    @Autowired
    private final TechTalentAgentRepository agentRepository;

    @Autowired
    private final EmployerRepository employerRepository;


    @Autowired
    private final UserRepository userRepo;


    @Autowired
    private final Helper helper;


    @Autowired
    ModelMapper mapper;

    @Autowired
    ApplicationService appService;



    @Override
    public String createTechTalent(TechTalentDTO techTalentDto, HttpServletRequest request) throws Exception {
        User loggedInUser = helper.extractLoggedInUser(request);

        Optional<EmployerResponse> employer_account= employerRepository.findByEmail(loggedInUser.getEmail());
        if (employer_account.isPresent()){            // an employer account does not exist
            throw new UserAlreadyExistException("An Employer account already exists!");
        }

        Optional<TechTalentAgent> agent_account = agentRepository.findByUserEmail(loggedInUser.getEmail()); // confirm
        if (agent_account.isPresent()){  // an agent account does not exist
            throw new UserAlreadyExistException("An Agent account already exists!");
        }

        Optional<TechTalentDTO> existingTechTalentUser = techTalentRepository.findByEmail(loggedInUser.getEmail());
        if (existingTechTalentUser.isPresent())
            throw new UserAlreadyExistException("Techtalent account already exists!");

        TechTalentUser techTalentUser = new TechTalentUser();

//     // TODO: YET TO BE TESTED!
//      TechTalentUser techTalentUser1 = (TechTalentUser)helper.copyFromDto(techTalentDto, techTalentUser1);

        techTalentUser.setEmail(loggedInUser.getEmail());
        techTalentUser.setBio(techTalentDto.getBio());
        techTalentUser.setSkills(techTalentDto.getSkills());
        techTalentUser.setResidentialAddress(techTalentDto.getResidentialAddress());
        techTalentUser.setJobType(techTalentDto.getJobType());
        techTalentUser.setHighestQualification(techTalentDto.getHighestQualification());
        techTalentUser.setExperienceLevel(techTalentDto.getExperienceLevel());
        techTalentUser.setYearsOfExperience(techTalentDto.getYearsOfExperience());
        techTalentUser.setCountryCode(techTalentDto.getCountryCode());
        techTalentUser.setWorkMode(techTalentDto.getWorkMode());
        techTalentUser.setCity(techTalentDto.getCity());
        techTalentUser.setZipCode(techTalentDto.getZipCode());
        techTalentUser.setCurrentJob(techTalentDto.getCurrentJob());
        techTalentUser.setLinkedInUrl(techTalentDto.getLinkedInUrl());
        techTalentUser.setLocation(techTalentDto.getLocation());
        techTalentUser.setState(techTalentDto.getState());
        techTalentUser.setResume(techTalentDto.getResume());
        techTalentUser.setCoverletter(techTalentDto.getCoverletter());
        techTalentUser.setProfessionalCert(techTalentDto.getProfessionalCert());
        techTalentUser.setProfilePicture(techTalentDto.getProfilePicture());
        techTalentUser.setBio(techTalentDto.getBio());
        techTalentUser.setPortfolioLink(techTalentDto.getPortfolioLink());
        techTalentUser.setJobInterest(techTalentDto.getJobInterest());
        loggedInUser.setUserType(UserType.TECHTALENT);
        loggedInUser.setTechTalent(techTalentUser);
        techTalentUser.setUser(loggedInUser);
        techTalentRepository.saveAndFlush(techTalentUser);
        userRepo.saveAndFlush(loggedInUser);

        return "TechTalent User created successfully";
    }




    @Override
    public TechTalentDTO getTechTalent(HttpServletRequest request) throws Exception{
        User loggedInUser = helper.extractLoggedInUser(request);

        return techTalentRepository.findByEmail(loggedInUser.getEmail())
                .orElseThrow(() -> new NotFoundException("Tech Talent not found"));
    }

//    @Override
//    public TechTalentUser updateTechTalent(String techId, Map<Object, Object> fields) throws Exception {
//        if (techId == null) {
//            throw new NotFoundException("Tech ID is required");}
//        Optional<TechTalentUser> techtalent = techTalentRepository.findById(Long.valueOf(techId));
//        if (techtalent.isPresent()) {
//            fields.forEach((key, value) -> {
//                        Field field = ReflectionUtils.findField(TechTalentUser.class, (String) key);
//
//                        if (field == null){
//                            throw new IllegalArgumentException("Fields cannot be null");
//                        }
//                        field.setAccessible(true);
//                        ReflectionUtils.setField(field, techtalent.get(), value);
//
//                    }
//            );
//             return techTalentRepository.save(techtalent.get());
//        }else
//        {
//            throw new NotFoundException("Tech Talent not found. Can't be updated!");
//        }
//
//    }

    @Override
    public TechTalentUser updateTechTalent(String techId, Map<Object, Object> fields) throws NotFoundException {
        if (techId == null) {
            throw new IllegalArgumentException("Tech ID is required");
        }

        Long techTalentId = Long.valueOf(techId);
        Optional<TechTalentUser> techTalentOptional = techTalentRepository.findById(techTalentId);

        if (techTalentOptional.isEmpty()) {
            throw new NotFoundException("Tech Talent not found with ID: " + techId);
        }

        TechTalentUser techTalent = techTalentOptional.get();

        // Update fields using reflection
        fields.forEach((fieldName, value) -> {
            try {
                Field field = TechTalentUser.class.getDeclaredField((String) fieldName);
                field.setAccessible(true);
                field.set(techTalent, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new IllegalArgumentException("Invalid field name: " + fieldName);
            }
        });

        // Save the updated TechTalentUser object
        return techTalentRepository.save(techTalent);
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
        User loggedInUser = helper.extractLoggedInUser(request);

        TechTalentDTO techTalentUser = techTalentRepository.findByUser(loggedInUser)
            .orElseThrow(() -> new UserNotFoundException("User not found"));


        UserResponseDto my_profile = userRepo.findByEmailAndEnabledTrue(loggedInUser.getEmail());


        List<ApplicationDTO> my_applications = appService.getMyApplications(request, pageable).getContent() ;


        List<SavedJobs> my_saved_jobs = appService.getMySavedJobs(request, pageable).getContent();


        DashboardDTO response = new DashboardDTO();


        response.setProfile(my_profile);
        response.setMy_applications(my_applications);
        response.setSaved_jobs(my_saved_jobs);
        response.setOther_profile(techTalentUser);

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



        response.add(myApplicationsLink);
        response.add(savedJobsLink);
        response.add(selfLink);
        return response;
    }

    @Override
    public void addNewSkills(HttpServletRequest request, List<String> skills) throws ExpiredJWTException {
        User loggedInUser = helper.extractLoggedInUser(request);
        Optional<TechTalentDTO> techTalentUser = techTalentRepository.findByEmail(loggedInUser.getEmail());
        if (techTalentUser.isPresent()){
            techTalentUser.get().setSkills(skills);
            ;
            techTalentRepository.save(mapper.map(techTalentUser.get(), TechTalentUser.class));
        }
    }
    

}





 



    

