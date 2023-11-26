package core.nxg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.dto.ApplicationDTO;
import core.nxg.entity.SavedJobs;
import jakarta.servlet.http.HttpServletRequest;

public interface ApplicationService {

    void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception;

    void updateApplication(ApplicationDTO applicationDTO);

    void saveJob(HttpServletRequest request, Long jobPostingId) throws Exception;

    Page<ApplicationDTO> getMyApplications(HttpServletRequest request, Pageable pageable)throws Exception;

    Page<SavedJobs> getMySavedJobs(HttpServletRequest request, Pageable pageable) throws Exception;


    
}
