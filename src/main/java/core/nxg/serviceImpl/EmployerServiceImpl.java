package core.nxg.serviceImpl;

import core.nxg.dto.EmployerDto;
import core.nxg.entity.Employer;
import core.nxg.entity.User;
import core.nxg.enums.UserType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.EmployerService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//TODO use map to dto properly
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final Helper helper;
    // private JavaLangInvokeAccess principal;

    @Override
    public List<EmployerDto> getAllEmployers() {

        //TODO return pageable
        return employerRepository.findAll()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }


    //TODO refactor to use HttpServletRequest
        @Override
        public Employer createEmployer (EmployerDto employerDto, Principal principal /*HttpServletRequest request*/){
            String loggedInUserEmail = principal.getName();

            //User user = helper.extractLoggedInUser(request);

            User user = (User)userRepository.findByEmail(loggedInUserEmail)
                    .orElseThrow(() -> new NotFoundException("User with email " + loggedInUserEmail + " not found"));

            Employer employer = new Employer();
            //TODO refactor code
            //helper.copyFromDto(employerDto,employer);
            employer.setCompanyName(employerDto.getCompanyName());
            employer.setCompanyDescription(employerDto.getCompanyDescription());
            employer.setPosition(employerDto.getPosition());
            employer.setCompanyAddress(employerDto.getCompanyAddress());
            employer.setCompanyWebsite(employerDto.getCompanyWebsite());
            employer.setCountry(employerDto.getCountry());
            employer.setIndustryType(employerDto.getIndustryType());
            employer.setUser(user);
            user.setRoles(UserType.EMPLOYER.name());

            userRepository.save(user);

            return employerRepository.save(employer);
        }

    @Override
    public EmployerDto getEmployerById(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
        return mapToDto(employer);
    }

    @Override
    public EmployerDto updateEmployer(Long employerId, EmployerDto employerDto) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
        employer.setCompanyName(employerDto.getCompanyName());
        employer.setCompanyDescription(employerDto.getCompanyDescription());
        employer.setPosition(employerDto.getPosition());
        employer.setCompanyAddress(employerDto.getCompanyAddress());
        employer.setCompanyWebsite(employerDto.getCompanyWebsite());
        employer.setCountry(employerDto.getCountry());
        employer.setIndustryType(employerDto.getIndustryType());

        employer = employerRepository.save(employer);

        return mapToDto(employer);
    }


    // You can implement other methods for managing employers here
    private EmployerDto mapToDto(Employer employer) {
        EmployerDto employerDto = new EmployerDto();
        BeanUtils.copyProperties(employer, employerDto);
        return employerDto;
    }


    //TODO return a response
    @Override
    public void deleteEmployer(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
        employerRepository.delete(employer);
    }
}

