package core.nxg.service;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.entity.JobPosting;
import core.nxg.response.EmployerResponse;
import core.nxg.response.EngagementForEmployer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface EmployerService {


    String createEmployer(EmployerDto employerDto, HttpServletRequest request) throws Exception;

    EmployerResponse getEmployer(HttpServletRequest request)throws Exception;

    Employer patchEmployer(String employerId, Map<Object, Object> fields) throws Exception;

    EngagementForEmployer getEngagements(String employerId,Pageable pageable) throws Exception;

    List<JobPosting> getJobPostings(String employerId) throws Exception;


        void deleteEmployer(String employerId);

}



