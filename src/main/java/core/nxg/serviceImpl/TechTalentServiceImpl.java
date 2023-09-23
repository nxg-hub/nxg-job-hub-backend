package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.service.TechTalentService;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.TechTalentDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TechTalentServiceImpl implements TechTalentService {

    @Autowired
    private final TechTalentRepository techTalentRepository;

   
    @Override
    public TechTalentUser createTechTalent(TechTalentDto TechTalenDto) {

        TechTalentUser user = new TechTalentUser();
        user.setPassword(TechTalenDto.getPassword());
        user.setEmail(TechTalenDto.getEmail());
        user.setNationality(TechTalenDto.getNationality());
        user.setSkills(null);
        user.setPhoneNumber(TechTalenDto.getPhoneNumber());
        user.setResidentialAddress(TechTalenDto.getResidentialAddress());
        user.setFirstName(TechTalenDto.getFirstName());
        user.setLastName(TechTalenDto.getLastName());
        user.setJobType(TechTalenDto.getJobType());
        user.setDateOfBirth(TechTalenDto.getDateOfBirth());
        user.setGender(TechTalenDto.getGender());
        user.setHighestQualification(TechTalenDto.getHighestQualification());
        user.setExperienceLevel(TechTalenDto.getExperienceLevel());
        user.setYearsOfExperience(TechTalenDto.getYearsOfExperience());
        user.setCountryCode(TechTalenDto.getCountryCode());
        return techTalentRepository.save(user);
    }       
    
}
