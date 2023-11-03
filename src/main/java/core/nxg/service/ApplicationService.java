package core.nxg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.dto.ApplicationDTO;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;

public interface ApplicationService {

    void createApplication(ApplicationDTO applicationDTO) throws Exception;

    void updateApplication(ApplicationDTO applicationDTO);

    Page<ApplicationDTO> getMyApplications(User myself, Pageable pageable)throws Exception;


    
}
