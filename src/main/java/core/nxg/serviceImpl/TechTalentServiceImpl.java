package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.TechTalentService;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.TechTalentDto;
import core.nxg.entity.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TechTalentServiceImpl implements TechTalentService {

    @Autowired
    private final TechTalentRepository techTalentRepository;

    @Autowired
    private final UserRepository userRepository;
   
    @Override
    public TechTalentUser createTechTalent(TechTalentDto TechTalenDto) {

        TechTalentUser techTalentUser = new TechTalentUser();

        User user = techTalentUser.getUser();

        
        //user.setPassword(TechTalenDto.getPassword());
        //user.setEmail(TechTalenDto.getEmail());
        techTalentUser.setNationality(TechTalenDto.getNationality());
        techTalentUser.setSkills(TechTalenDto.getSkills());
        //techTalentUser.setPhoneNumber(TechTalenDto.getPhoneNumber());
        techTalentUser.setResidentialAddress(TechTalenDto.getResidentialAddress());
        // user.setFirstName(TechTalenDto.getFirstName());
        //user.setLastName(TechTalenDto.getLastName());
        techTalentUser.setJobType(TechTalenDto.getJobType());
        //user.setDateOfBirth(TechTalenDto.getDateOfBirth());
        //user.setGender(TechTalenDto.getGender());
        techTalentUser.setHighestQualification(TechTalenDto.getHighestQualification());
        techTalentUser.setExperienceLevel(TechTalenDto.getExperienceLevel());
        techTalentUser.setYearsOfExperience(TechTalenDto.getYearsOfExperience());
        techTalentUser.setCountryCode(TechTalenDto.getCountryCode());
        techTalentUser.setWorkMode(TechTalenDto.getWorkMode());
        techTalentUser.setProfessionalCert(TechTalenDto.getProfessionalCert());
        userRepository.save(user);
        return techTalentRepository.save(techTalentUser);
    }       
    
}
