//package core.nxg.serviceImpl;
//
//import core.nxg.dto.TechTalentProfileDto;
//import core.nxg.entity.TechTalentProfile;
//import core.nxg.repository.TechTalentAgentRepository;
//import core.nxg.repository.TechTalentProfileRepository;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//public class TechTalentProfileServiceImpl {
//    private final TechTalentProfileRepository techTalentProfileRepository;
//
//    @Override
//    public TechTalentProfile createTechTalentProfile(TechTalentProfileDto techTalentProfileDto){
//        TechTalentProfile techTalentProfile = new TechTalentProfile();
//        techTalentProfile.setfullName(techTalentProfileDto.getfullName());
//        return techTalentProfileRepository.saveAndFlush(techTalentProfile);
//    }
//}
