package core.nxg.service;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface EmployerService {


    String createEmployer(EmployerDto employerDto, HttpServletRequest request) throws Exception;

    EmployerDto getEmployer(HttpServletRequest request)throws Exception;

    Object updateEmployer(Long employerId, EmployerDto employerDto) throws Exception;
    Employer patchEmployer(String employerId, Map<Object, Object> fields) throws Exception;


        void deleteEmployer(Long employerId);
}



