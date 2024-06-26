package core.nxg.service;

import core.nxg.entity.JobPosting;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.dto.ApplicationDTO;
import core.nxg.entity.SavedJobs;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.MailException;

import java.io.UnsupportedEncodingException;

public interface ApplicationService {

    void createApplication(HttpServletRequest request, ApplicationDTO applicationDTO) throws Exception;

    void saveJob(HttpServletRequest request, Long jobPostingId) throws Exception;

    Page<ApplicationDTO> getMyApplications(HttpServletRequest request, Pageable pageable) throws Exception;

    Page<SavedJobs> getMySavedJobs(HttpServletRequest request, Pageable pageable) throws Exception;




}
