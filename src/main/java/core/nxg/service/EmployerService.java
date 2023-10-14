package core.nxg.service;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;

import java.security.Principal;
import java.util.List;

public interface EmployerService {
    List<EmployerDto> getAllEmployers();

    // Employer createEmployer (EmployerDto employerDto);

    Employer createEmployer(EmployerDto employerDto, Principal principal);

    EmployerDto getEmployerById(Long employerId);

    EmployerDto updateEmployer(Long employerId, EmployerDto employerDto);

    void deleteEmployer(Long employerId);
}



