package core.nxg.service;

import java.util.List;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;


public interface TechTalentService {
    TechTalentUser createTechTalent(TechTalentDTO techTalentDto) throws Exception;
    List<TechTalentUser> getAllTechTalent() throws Exception;
   
        
}
