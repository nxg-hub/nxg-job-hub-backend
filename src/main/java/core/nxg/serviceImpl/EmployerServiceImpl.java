package core.nxg.serviceImpl;

import core.nxg.configs.JwtService;
import core.nxg.dto.EmployerDto;

import core.nxg.entity.Employer;

import core.nxg.entity.Ratings;
import core.nxg.entity.User;
import core.nxg.exceptions.NotFoundException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.UserRepository;
import core.nxg.service.EmployerService;
import core.nxg.service.RatingsService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class EmployerServiceImpl implements EmployerService {

    private final Helper helper;
    private final EmployerRepository employerRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    //private final RatingsService ratings;


    @Override
    public String verifyEmployer(String email, HttpServletRequest request) {
        try {
            User user = extractLoggedInUser(request, email);
            if (user != null) {
                Employer existingEmployer = (Employer) employerRepository.findByEmail(email).orElse(null);
                if (existingEmployer != null) {
                    return "Employer already verified";
                } else {
                    Employer newEmployer = new Employer();
                    newEmployer.setEmail(email);
                    newEmployer.setUser(user);
                    employerRepository.save(newEmployer);
                    return "Employer verified and created";
                }
            } else {
                throw new NotFoundException("Email not found in the user repository");
            }
        } catch (Exception e) {
            return "Error verifying Employer: " + e.getMessage();

    }
    }
    @Override
    public String createEmployer(String email, EmployerDto employerDTO) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

            Employer existingEmployer = (Employer) employerRepository.findByUser(user).orElse(null);
            if (existingEmployer != null) {
                return "Employer already exists for this user";
            } else {
                Employer employer = new Employer();
                employer.setCompanyName(employerDTO.getCompanyName());
                // Set other properties

                employer.setCompanyDescription(employerDTO.getCompanyDescription());
            employer.setPosition(employerDTO.getPosition());
            employer.setCompanyAddress(employerDTO.getCompanyAddress());
            employer.setCompanyWebsite(employerDTO.getCompanyWebsite());
            employer.setCountry(employerDTO.getCountry());
            employer.setIndustryType(employerDTO.getIndustryType());
            employer.setCompanySize(employerDTO.getCompanySize());
            //employer.setRatings(employerDTO.getRatings(List< Ratings > ratings));

            //employer.setUser(employerDTO.getUser());
                employer.setUser(user);
                employerRepository.save(employer);
                return "Employer successfully registered";
            }
        } catch (Exception e) {
            return "Error creating employer: " + e.getMessage();
        }
    }

    private User extractLoggedInUser(HttpServletRequest request, String email) {
        // ... (extractLoggedInUser logic)
        final String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        String extractedEmail = jwtService.extractUsername(jwt);
        if (email.equals(extractedEmail)) {
            return userRepository.findByEmail(email).orElse(null);
        } else {
            return null;
        }
    }

    @Override
    public String updateEmployer(Long Id, EmployerDto employerDto) {
        // Check if employerDto or its properties are null and handle the validation as needed
        // ...

        try {
            Employer employer = employerRepository.findById(Id)
                    .orElseThrow(() -> new NotFoundException("TechTalentEmployer with ID " + Id + " not found"));

            // Update only non-null properties from the DTO
            if (employerDto.getCompanyName() != null) {
                employer.setCompanyName(employerDto.getCompanyName());
            }
            if (employerDto.getCompanyDescription() != null) {
                employer.setCompanyDescription(employerDto.getCompanyDescription());
            }
            // Repeat this pattern for other properties

            // Save the updated employer entity
            employerRepository.save(employer);

            // Return a success message
            return "Employer with ID " + Id + " successfully updated.";
        } catch (Exception e) {
            // Handle any exceptions that might occur during the update operation
            return "Error updating employer: " + e.getMessage();
        }
    }



//    @Override
//    public EmployerDto updateEmployer(Long employerId, EmployerDto employerDto) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("TechTalentEmployer with ID " + employerId + " not found"));
//        employer.setCompanyName(employerDto.getCompanyName());
//        employer.setCompanyDescription(employerDto.getCompanyDescription());
//        employer.setIndustryType(employerDto.getIndustryType());
//        employer.setCompanyAddress(employerDto.getCompanyAddress());
//        employer.setPosition(employerDto.getPosition());
//        employer.setCountry(employerDto.getCountry());
//        employer.setCompanySize(employerDto.getCompanySize());
//        //techTalentEmployer.setIndustryType(techTalentEmployerDto.getIndustryType());
//        employer = employerRepository.save(employer);
//        return mapToDto(employer);
//    }



    @Override
    public void deleteEmployer(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("TechTalentEmployer with ID " + employerId + " not found"));
        employerRepository.delete(employer);
    }

    @Override
    public EmployerDto getEmployerById(Long Id) {
        Employer employer = employerRepository.findById(Id)
                .orElseThrow(() -> new NotFoundException("Employer with ID " + Id + " not found"));
        return mapToDto(employer);
    }

    @Override
    public List<EmployerDto> getAllEmployer() {
        return employerRepository.findAll()
                .stream().map(this::mapToDto).collect(Collectors.toList());
    }




    private EmployerDto mapToDto(Employer employer) {
        EmployerDto employerDto = new EmployerDto();
        BeanUtils.copyProperties(employer, employerDto);
        return employerDto;
    }

}



































//
//package core.nxg.serviceImpl;
//
//import core.nxg.dto.EmployerDto;
//import core.nxg.entity.Employer;
//import core.nxg.entity.User;
//import core.nxg.enums.UserType;
//import core.nxg.exceptions.NotFoundException;
//import core.nxg.repository.EmployerRepository;
//import core.nxg.repository.UserRepository;
//import core.nxg.service.EmployerService;
//import core.nxg.utils.Helper;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.http.HttpRequest;
//import org.springframework.stereotype.Service;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
////TODO use map to dto properly
//public class EmployerServiceImpl implements EmployerService {
//
//    private final EmployerRepository employerRepository;
//    private final UserRepository userRepository;
//    private final Helper helper;
//    // private JavaLangInvokeAccess principal;
//
//    @Override
//    public List<EmployerDto> getAllEmployers() {
//
//        //TODO return pageable
//        return employerRepository.findAll()
//                .stream().map(this::mapToDto).collect(Collectors.toList());
//    }
//
//
//    //TODO refactor to use HttpServletRequest
//        @Override
//        public Employer createEmployer (EmployerDto employerDto, Principal principal /*HttpServletRequest request*/){
//            String loggedInUserEmail = principal.getName();
//
//            //User user = helper.extractLoggedInUser(request);
//
//            User user = userRepository.findByEmail(loggedInUserEmail)
//                    .orElseThrow(() -> new NotFoundException("User with email " + loggedInUserEmail + " not found"));
//
//            Employer employer = new Employer();
//            //TODO refactor code
//            //helper.copyFromDto(employerDto,employer);
//            employer.setCompanyName(employerDto.getCompanyName());
//            employer.setCompanyDescription(employerDto.getCompanyDescription());
//            employer.setPosition(employerDto.getPosition());
//            employer.setCompanyAddress(employerDto.getCompanyAddress());
//            employer.setCompanyWebsite(employerDto.getCompanyWebsite());
//            employer.setCountry(employerDto.getCountry());
//            employer.setIndustryType(employerDto.getIndustryType());
//            employer.setUser(user);
//            user.setRoles(UserType.EMPLOYER.name());
//
//            userRepository.save(user);
//
//            return employerRepository.save(employer);
//        }
//
//    @Override
//    public EmployerDto getEmployerById(Long employerId) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
//        return mapToDto(employer);
//    }
//
//    @Override
//    public EmployerDto updateEmployer(Long employerId, EmployerDto employerDto) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
//        employer.setCompanyName(employerDto.getCompanyName());
//        employer.setCompanyDescription(employerDto.getCompanyDescription());
//        employer.setPosition(employerDto.getPosition());
//        employer.setCompanyAddress(employerDto.getCompanyAddress());
//        employer.setCompanyWebsite(employerDto.getCompanyWebsite());
//        employer.setCountry(employerDto.getCountry());
//        employer.setIndustryType(employerDto.getIndustryType());
//
//        employer = employerRepository.save(employer);
//
//        return mapToDto(employer);
//    }
//
//
//    // You can implement other methods for managing employers here
//    private EmployerDto mapToDto(Employer employer) {
//        EmployerDto employerDto = new EmployerDto();
//        BeanUtils.copyProperties(employer, employerDto);
//        return employerDto;
//    }
//
//
//    //TODO return a response
//    @Override
//    public void deleteEmployer(Long employerId) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
//        employerRepository.delete(employer);
//    }
//}
//
//
//
//    @Override
//    public EmployerDto getEmployerById(Long employerId) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
//        return mapToDto(employer);
//    }
//
//    @Override
//    public EmployerDto updateEmployer(Long employerId, EmployerDto employerDto) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
//        employer.setCompanyName(employerDto.getCompanyName());
//        employer.setCompanyDescription(employerDto.getCompanyDescription());
//        employer.setPosition(employerDto.getPosition());
//        employer.setCompanyAddress(employerDto.getCompanyAddress());
//        employer.setCompanyWebsite(employerDto.getCompanyWebsite());
//        employer.setCountry(employerDto.getCountry());
//        employer.setIndustryType(employerDto.getIndustryType());
//        employer = employerRepository.save(employer);
//        return mapToDto(employer);
//    }
//
//    @Override
//    public void deleteEmployer(Long employerId) {
//        Employer employer = employerRepository.findById(employerId)
//                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
//        employerRepository.delete(employer);
//    }
//
//    private EmployerDto mapToDto(Employer employer) {
//        EmployerDto employerDto = new EmployerDto();
//        BeanUtils.copyProperties(employer, employerDto);
//        return employerDto;
//    }
//}
