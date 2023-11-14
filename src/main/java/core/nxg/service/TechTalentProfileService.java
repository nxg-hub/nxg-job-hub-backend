package core.nxg.service;


import core.nxg.dto.TechTalentProfileDto;
import core.nxg.entity.TechTalentProfile;

public interface TechTalentProfileService {
    TechTalentProfile createTechTalentProfile(TechTalentProfileDto techTalentProfileDto);
}
