package core.nxg.service;

import core.nxg.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.dto.ApplicationDTO;
import core.nxg.entity.SavedJobs;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ApplicationService {

    void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception;

    void saveJob(HttpServletRequest request, String jobPostingId) throws Exception;

    void acceptApplication(String applicationId, HttpServletRequest request) throws Exception;

    void rejectApplication(String applicationId, HttpServletRequest request) throws Exception;

    Page<Application> getMyApplications(HttpServletRequest request, Pageable pageable) throws Exception;

    Page<SavedJobs> getMySavedJobs(HttpServletRequest request, Pageable pageable) throws Exception;


    List<Application> getSuggestedApplicants(String jobId, int scoreThreshold);
}
