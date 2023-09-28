package core.nxg.service;

import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.TechTalentUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TechTalentService<T> {
    TechTalentDTO createTechTalent(T techTalentDto) throws Exception;
    Page<TechTalentUser> getAllTechTalent(TechTalentDTO techTalentDto, Pageable pageable) throws Exception; //this should be  pageable and not list
    //TechTalentUser  deleteTechTalentUser(Long techId) throws Exception;
   
        
}
