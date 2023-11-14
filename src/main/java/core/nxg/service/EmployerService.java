package core.nxg.service;

import core.nxg.dto.EmployerDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface EmployerService {



    String verifyEmployer(String email, HttpServletRequest request);

    String createEmployer(String email, EmployerDto employerDTO);

    String updateEmployer(Long employerId, EmployerDto employerDto);

    void deleteEmployer(Long employerId);

    EmployerDto getEmployerById(Long Id);

    List<EmployerDto> getAllEmployer();

    //String createEmployer(String email, EmployerDto employerDTO);
}





