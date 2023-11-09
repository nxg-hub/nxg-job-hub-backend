package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.TechTalentService;
import core.nxg.utils.SkillNames;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.Skill;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.TechTalentDTO;
import core.nxg.dto.UserDTO;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import core.nxg.exceptions.UserNotFoundException;

import java.util.ArrayList;
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
        Optional<User> userOptional = userRepository.findByEmail(techTalentDto.getEmail());

            if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Account with this email does not exist. Create an account!");}

        TechTalentUser techTalentUser = new TechTalentUser();
// =======


//         TechTalentUser techTalentUser = new TechTalentUser(); 
// >>>>>>> main

        User user = userOptional.get();        


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
        techTalentUser.setUser(user);
        techTalentRepository.saveAndFlush(techTalentUser);

        return techTalentDto;          


    } ;
    
    @Override
    public Page<TechTalentDTO> getAllTechTalent(Pageable pageable) throws Exception {
        Page<TechTalentUser> techTalentUser = techTalentRepository.findAll(pageable);
        return techTalentUser.map(TechTalentDTO::new);
       
    }



    

};









    

