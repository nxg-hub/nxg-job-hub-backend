package core.nxg.service;

import core.nxg.dto.TechTalentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TechTalentService<T> {
    TechTalentDTO createTechTalent(TechTalentDTO techTalentDto) throws Exception;
    Page<TechTalentDTO> getAllTechTalent(Pageable pageable) throws Exception; //this should be  pageable and not list
    void  deleteTechTalentUser(Long techId) throws Exception;
    TechTalentDTO getTechTalentById(Long Id)  throws Exception;
    TechTalentDTO updateTechTalent(TechTalentDTO techTalentDto, Long techId) throws Exception;
        
}
