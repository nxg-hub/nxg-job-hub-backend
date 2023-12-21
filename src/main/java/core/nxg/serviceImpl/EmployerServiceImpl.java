package core.nxg.serviceImpl;

import core.nxg.dto.EmployerDto;
import core.nxg.dto.TechTalentDTO;
import core.nxg.entity.*;
import core.nxg.enums.ApplicationStatus;
import core.nxg.enums.UserType;
import core.nxg.exceptions.NotFoundException;
import core.nxg.exceptions.UserAlreadyExistException;
import core.nxg.repository.*;

import core.nxg.response.EmployerResponse;
import core.nxg.response.EngagementForEmployer;
import core.nxg.response.JobPostingResponse;
import core.nxg.service.EmployerService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.beans.FeatureDescriptor;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.util.ReflectionUtils;

@Service
@RequiredArgsConstructor
//TODO use map to dto properly
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final Helper helper;
    private final JobPostingRepository jobPostingRepository;
    private final TechTalentRepository techTalentRepository;
    private final TechTalentAgentRepository agentRepository;
    private final ModelMapper mapper;




        @Override
        public String createEmployer (EmployerDto employerDto, HttpServletRequest request) throws Exception{

            User loggedInUser = helper.extractLoggedInUser(request);

            String loggedInUserEmail = loggedInUser.getEmail();

            Optional<EmployerResponse >user = employerRepository.findByEmail(loggedInUserEmail);
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
            //TODO refactor code to use beanutils to copy properties
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
    public EmployerResponse getEmployer(HttpServletRequest request) throws Exception{
        User loggedInUser = helper.extractLoggedInUser(request);

        return employerRepository.findByEmail(loggedInUser.getEmail())
                .orElseThrow(() -> new NotFoundException("Employer not found"));
    }





    @Override
    public ResponseEntity<Employer> updateEmployer(Long id, EmployerDto employerdto) {
    Optional<Employer> employer = employerRepository.findById(id);
// TODO: refactor code
    return null;
}

//    public JobPostingResponse getJobPostingsByEmployerId(Long employerId){
//        JobPosting jobPosting = jobPostingRepository.findJobPostingByEmployerID(employerId)
//                .orElseThrow(() -> new NotFoundException("No job postings found for the employer!"));
//        JobPostingResponse jobPostings = new JobPostingResponse();
//        mapper.map(jobPosting, jobPostings);
//        return jobPostings;
//    }







    @Override
    public Employer patchEmployer(String employerId, Map<Object, Object> fields) throws Exception {
            if (employerId == null) {
                throw new NotFoundException("Employer ID is required");}
            Optional<Employer> employer = employerRepository.findById(Long.valueOf(employerId));
            if (employer.isPresent()) {
                fields.forEach((key, value) -> {
                    Field field = ReflectionUtils.findField(Employer.class, String.valueOf(key));
                    if (field == null) {
                        throw new IllegalArgumentException("Null fields cannot be updated! Kindly check the fields you are trying to update");
                    }
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




    @Override
    public void deleteEmployer(Long employerId) {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer with ID " + employerId + " not found"));
        //TODO return a response

        employerRepository.delete(employer);
    }

    public EngagementForEmployer getEngagements(Long employerId) throws Exception {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer was not found!"));

        JobPosting existingJobPostings = jobPostingRepository.findJobPostingByEmployerID(employerId)
                .orElseThrow(() -> new NotFoundException("Job Postings were not found!"));


        List<JobPosting> jobPostings = Collections.singletonList(existingJobPostings);

        List<Application> applications = applicationRepository.findByJobPosting((JobPosting) jobPostings.stream(), Pageable.unpaged())
                .orElseThrow(() -> new NotFoundException("Applications were not found!"));


        int applicants = applications.stream().map(Application::getApplicant).toList().size();
        int numberOfJobs = jobPostings.size();
        int noOfApprovedJobs = applications.stream()
                .filter(x -> x.getApplicationStatus()
                        .equals(ApplicationStatus.APPROVED)).toList().size();
        EngagementForEmployer engagements = new EngagementForEmployer();
        engagements.setNoOfApplicants(applicants);
        engagements.setNoOfJobPostings(numberOfJobs);
        engagements.setNoOfApprovedApplications(noOfApprovedJobs);
        return engagements;
    }


    @Override
    public JobPostingResponse getJobPostings(Long employerId) throws Exception{
            JobPosting jobPosting = jobPostingRepository.findJobPostingByEmployerID(employerId)
                    .orElseThrow(() -> new NotFoundException("No job postings found for the employer!"));
            JobPostingResponse jobPostings = new JobPostingResponse();
            mapper.map(jobPosting, jobPostings);
            return jobPostings;
    }
}

