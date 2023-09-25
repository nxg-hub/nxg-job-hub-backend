package core.nxg.service;

import java.util.List;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;
import org.springframework.data.domain.Page;


public interface TechTalentService {
    TechTalentUser createTechTalent(TechTalentDTO techTalentDto) throws Exception;
    Page<TechTalentUser> getAllTechTalent() throws Exception; //this should be  pageable and not list
   
        
}
