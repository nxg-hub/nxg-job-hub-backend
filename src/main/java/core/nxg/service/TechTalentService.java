package core.nxg.service;

import core.nxg.dto.TechTalentDto;
import core.nxg.entity.TechTalentUser;


public interface TechTalentService {
    TechTalentUser createTechTalent(TechTalentDto techTalentDto);

   
        
}
