package core.nxg.service;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.response.EmployerResponse;
import core.nxg.response.EngagementForEmployer;
import core.nxg.response.JobPostingResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EmployerService {


    String createEmployer(EmployerDto employerDto, HttpServletRequest request) throws Exception;

    EmployerResponse getEmployer(HttpServletRequest request)throws Exception;

    Object updateEmployer(Long employerId, EmployerDto employerDto) throws Exception;
    Employer patchEmployer(String employerId, Map<Object, Object> fields) throws Exception;

    EngagementForEmployer getEngagements(Long employerId,Pageable pageable) throws Exception;

    JobPostingResponse getJobPostings(String employerId, Pageable pageable) throws Exception;


        void deleteEmployer(Long employerId);
}



