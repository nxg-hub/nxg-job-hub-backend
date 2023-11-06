package core.nxg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.dto.ApplicationDTO;
import core.nxg.entity.SavedJobs;
import core.nxg.entity.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ApplicationService {

    void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception;

    void updateApplication(ApplicationDTO applicationDTO);

    Page<ApplicationDTO> getMyApplications(HttpServletRequest request, Pageable pageable)throws Exception;

    Page<SavedJobs> getMySavedJobs(HttpServletRequest request, Pageable pageable) throws Exception;


    
}
