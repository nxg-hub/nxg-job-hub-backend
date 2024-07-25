package core.nxg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.dto.ApplicationDTO;
import core.nxg.entity.SavedJobs;
import jakarta.servlet.http.HttpServletRequest;

public interface ApplicationService {

    void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception;

    void saveJob(HttpServletRequest request, String jobPostingId) throws Exception;

    void acceptApplication(String applicationId, HttpServletRequest request) throws Exception;

    void rejectApplication(String applicationId, HttpServletRequest request) throws Exception;

    Page<ApplicationDTO> getMyApplications(HttpServletRequest request, Pageable pageable) throws Exception;

    Page<SavedJobs> getMySavedJobs(HttpServletRequest request, Pageable pageable) throws Exception;




}
