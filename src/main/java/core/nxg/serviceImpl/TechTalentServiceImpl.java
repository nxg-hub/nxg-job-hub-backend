package core.nxg.serviceImpl;

import core.nxg.repository.TechTalentRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.TechTalentService;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.TechTalentUser;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.User;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import core.nxg.exceptions.UserAlreadyExistException;

@Service
@RequiredArgsConstructor
public class TechTalentServiceImpl<T extends TechTalentDTO> implements TechTalentService<T> {

    @Autowired
    private final TechTalentRepository techTalentRepository;

    @Autowired 
    private final UserRepository userRepository;
   
    @Override
    public TechTalentDTO createTechTalent(TechTalentDTO techTalentDto) throws Exception {
        Optional<User> user = userRepository.findByEmail(techTalentDto.getEmail());
        if (user.isPresent()) {
            throw new UserAlreadyExistException("User already exists.");
        }
        else{
        //
        TechTalentUser techTalentUser = new TechTalentUser();
        User user1 = techTalentUser.getUser();
        user1.setEmail(techTalentDto.getEmail());
        user.get().setId(user1.getId());


        techTalentUser.setNationality(techTalentDto.getNationality());
        techTalentUser.setSkills(techTalentDto.getSkills());
        techTalentUser.setResidentialAddress(techTalentDto.getResidentialAddress());
        techTalentUser.setJobType(techTalentDto.getJobType());
        techTalentUser.setHighestQualification(techTalentDto.getHighestQualification());
        techTalentUser.setExperienceLevel(techTalentDto.getExperienceLevel());
        techTalentUser.setYearsOfExperience(techTalentDto.getYearsOfExperience());
        techTalentUser.setCountryCode(techTalentDto.getCountryCode());
        techTalentUser.setWorkMode(techTalentDto.getWorkMode());
        techTalentUser.setProfessionalCert(techTalentDto.getProfessionalCert());
    
        techTalentRepository.save(techTalentUser);}
        return techTalentDto;
    }       
    
    @Override
    public Page<TechTalentUser> getAllTechTalent(TechTalentDTO techTalentDto, Pageable pageable) throws Exception {
        return techTalentRepository.findAll((Pageable) pageable);
       
    }
}
