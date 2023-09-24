package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.TechTalentService;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.User;
import java.util.List;

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
    public TechTalentUser createTechTalent(TechTalentDTO TechTalentDto) throws Exception {
        User user = userRepository.findByEmail(TechTalentDto.getEmail());

            if (user != null) {
                    throw new Exception("User already exist.");
            }
        TechTalentUser techTalentUser = new TechTalentUser();


       
        techTalentUser.setNationality(TechTalentDto.getNationality());
        techTalentUser.setSkills(TechTalentDto.getSkills());
        techTalentUser.setResidentialAddress(TechTalentDto.getResidentialAddress());
        techTalentUser.setJobType(TechTalentDto.getJobType());
        techTalentUser.setHighestQualification(TechTalentDto.getHighestQualification());
        techTalentUser.setExperienceLevel(TechTalentDto.getExperienceLevel());
        techTalentUser.setYearsOfExperience(TechTalentDto.getYearsOfExperience());
        techTalentUser.setCountryCode(TechTalentDto.getCountryCode());
        techTalentUser.setWorkMode(TechTalentDto.getWorkMode());
        techTalentUser.setProfessionalCert(TechTalentDto.getProfessionalCert());
        //techTalentUser.setUser(user);     
        return techTalentRepository.save(techTalentUser);
    }       
    
    @Override
    public List<TechTalentUser> getAllTechTalent() throws Exception{

        List<TechTalentUser> users = techTalentRepository.findAll();
        return users;
    }
}
