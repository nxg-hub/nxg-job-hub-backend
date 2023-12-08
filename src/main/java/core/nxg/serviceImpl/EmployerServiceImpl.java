package core.nxg.serviceImpl;

import core.nxg.dto.EmployerDto;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.Employer;
import core.nxg.entity.TechTalentAgent;
import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.enums.UserType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.repository.EmployerRepository;
import core.nxg.repository.UserRepository;
import core.nxg.repository.TechTalentAgentRepository;

import core.nxg.service.EmployerService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.beans.FeatureDescriptor;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.nxg.repository.TechTalentRepository;
import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
//TODO use map to dto properly
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final Helper helper;
    private final TechTalentRepository techTalentRepository;
    private final TechTalentAgentRepository agentRepository;
    private final ModelMapper mapper;




        @Override
        public String createEmployer (EmployerDto employerDto, HttpServletRequest request) throws Exception{

            User loggedInUser = helper.extractLoggedInUser(request);

            String loggedInUserEmail = loggedInUser.getEmail();

            Optional<EmployerDto >user = employerRepository.findByEmail(loggedInUserEmail);
            if (user.isPresent()){ // an employer account already exists
                throw new UserAlreadyExistException("An Employer account already exists!");
            }

            Optional<TechTalentDTO> talent_account = techTalentRepository.findByEmail(loggedInUserEmail); //confirm
            if (talent_account.isPresent()){            // a tech talent account does not exist
                throw new UserAlreadyExistException("A TechTalent account already exists!");
            }

            Optional<TechTalentAgent> agent_account = agentRepository.findByUserEmail(loggedInUserEmail); // confirm
            if (agent_account.isPresent()){  // an agent account does not exist
                throw new UserAlreadyExistException("An Agent account already exists!");
            }


            Employer employer = new Employer();
            //TODO refactor code
            //helper.copyFromDto(employerDto,employer);
            employer.setCompanyName(employerDto.getCompanyName());
            employer.setEmail(loggedInUser.getEmail());
            employer.setCompanyDescription(employerDto.getCompanyDescription());
            employer.setPosition(employerDto.getPosition());
            employer.setCompanyAddress(employerDto.getCompanyAddress());
            employer.setCompanyWebsite(employerDto.getCompanyWebsite());
            employer.setCountry(employerDto.getCountry());
            employer.setIndustryType(employerDto.getIndustryType());
            employer.setCompanySize(employerDto.getCompanySize());
            loggedInUser.setRoles(UserType.EMPLOYER.toString());
            loggedInUser.setUserType(UserType.EMPLOYER);
            userRepository.save(loggedInUser);

            employer.setUser(loggedInUser);



            employerRepository.saveAndFlush(employer);
            return "Employer created successfully!";
        }

    @Override
    public EmployerDto getEmployer(HttpServletRequest request) throws Exception{
        User loggedInUser = helper.extractLoggedInUser(request);

        return employerRepository.findByEmail(loggedInUser.getEmail())
                .orElseThrow(() -> new NotFoundException("Employer not found"));
    }





    @Override
    public ResponseEntity<Employer> updateEmployer(Long id, EmployerDto employerdto) {
    // Find the employer with the given id
    Optional<Employer> employer = employerRepository.findById(id);



    // Return null as the response
    return null;
}







    @Override
    public Employer patchEmployer(String employerId, Map<Object, Object> fields) throws Exception {
            if (employerId == null) {
                throw new NotFoundException("Employer ID is required");}
            Optional<Employer> employer = employerRepository.findById(Long.valueOf(employerId));
            if (employer.isPresent()) {
                fields.forEach((key, value) -> {
                    Field field = ReflectionUtils.findField(Employer.class, String.valueOf(key));
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, employer.get(), value);

                }
                );
                return employerRepository.save(employer.get());
            }else
            {
                throw new NotFoundException("Employer not found");
            }

        }



    // You can implement other methods for managing employers here
//    private EmployerDto mapToDto(Employer employer) {
//        EmployerDto employerDto = new EmployerDto();
//        BeanUtils.copyProperties(employer, employerDto);
//        return employerDto;
//    }


    //TODO return a response
    @Override
    public void deleteEmployer(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
        employerRepository.delete(employer);
    }
}

