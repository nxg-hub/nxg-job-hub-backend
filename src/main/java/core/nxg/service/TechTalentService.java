package core.nxg.service;

import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TechTalentService<T> {
    TechTalentDTO createTechTalent(TechTalentDTO techTalentDto) throws Exception;
    Page<TechTalentDTO> getAllTechTalent(Pageable pageable) throws Exception; //this should be  pageable and not list
    //TechTalentUser  deleteTechTalentUser(Long techId) throws Exception;
   
        
}
