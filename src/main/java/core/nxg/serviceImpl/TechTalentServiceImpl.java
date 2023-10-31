package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.TechTalentService;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.Skill;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.exceptions.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechTalentServiceImpl<T extends TechTalentDTO> implements TechTalentService<T> {

    @Autowired
    private final TechTalentRepository techTalentRepository;

    @Autowired 
    private final UserRepository userRepository;
   
    @Override
    public TechTalentDTO createTechTalent(TechTalentDTO techTalentDto) throws Exception {
          // Check if the user with the provided email exists


        User userOptional = userRepository.findByEmail(techTalentDto.getEmail())
        .orElseThrow(() -> new UserNotFoundException("Account with this email does not exist. Create an account!"));

        TechTalentUser existingTechTalentUser = techTalentRepository.findByUser(userOptional)
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
        };
        
        techTalentUser.setCoverletter(techTalentDto.getCoverletter());
        techTalentUser.setProfessionalCert(techTalentDto.getProfessionalCert());
        userOptional.setTechTalent(techTalentUser);
        techTalentUser.setUser(userOptional);
        techTalentRepository.saveAndFlush(techTalentUser);

        return techTalentDto;          


    } ;
            

    
  
    
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


    

};
    @Override
    public void deleteTechTalentUser(Long ID){
        TechTalentUser user = techTalentRepository.findById(ID)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        techTalentRepository.delete(user);
    }


}









    

