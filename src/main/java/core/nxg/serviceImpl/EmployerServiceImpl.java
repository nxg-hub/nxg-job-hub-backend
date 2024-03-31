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
import core.nxg.service.EmployerService;
import core.nxg.utils.Helper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.ReflectionUtils;
import core.nxg.enums.Roles;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    @Autowired
    private final EmployerRepository employerRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ApplicationRepository applicationRepository;
    @Autowired
    private final Helper helper;

    @Autowired
    private final JobPostingRepository jobPostingRepository;

    @Autowired
    private final TechTalentRepository techTalentRepository;
    @Autowired
    private final TechTalentAgentRepository agentRepository;
    @Autowired
    private final ModelMapper mapper;




        @Override
        public String createEmployer (EmployerDto employerDto, HttpServletRequest request) throws Exception{

            User loggedInUser = helper.extractLoggedInUser(request);

            String loggedInUserEmail = loggedInUser.getEmail();

            Optional<EmployerResponse >user = employerRepository.findByEmail(loggedInUserEmail);// confirm
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

            employer.setCompanyName(employerDto.getCompanyName());
            employer.setEmail(loggedInUser.getEmail());
            employer.setCompanyDescription(employerDto.getCompanyDescription());
            employer.setPosition(employerDto.getPosition());
            employer.setCompanyAddress(employerDto.getCompanyAddress());
            employer.setCompanyWebsite(employerDto.getCompanyWebsite());
            employer.setCountry(employerDto.getCountry());
            employer.setIndustryType(employerDto.getIndustryType());
            employer.setCACCertificate(employerDto.getCACCertificate());
            employer.setCompanyMemorandum(employerDto.getCompanyMemorandum());
            employer.setNamesOfDirectors(employerDto.getNamesOfDirectors());
            employer.setTIN(employerDto.getTIN());
            employer.setTaxClearanceCertificate(employerDto.getTaxClearanceCertificate());
            employer.setCompanySize(employerDto.getCompanySize());
            employer.setAddress(employerDto.getAddress());
            employer.setNationality(employerDto.getNationality());
            employer.setState(employerDto.getState());
            employer.setZipCode(employerDto.getZipCode());
            employer.setCompanyZipCode(employerDto.getCompanyZipCode());
            employer.setVacancies(employerDto.getVacancies());
            loggedInUser.setRoles(List.of(Roles.USER));

            loggedInUser.setUserType(UserType.EMPLOYER);
            userRepository.save(loggedInUser);

            employer.setUser(loggedInUser);
            employerRepository.saveAndFlush(employer);
            loggedInUser.setEmployer(employer);
            userRepository.save(loggedInUser);
            return "Employer created successfully!";
        }

    @Override
    public EmployerResponse getEmployer(HttpServletRequest request) throws Exception{
        User loggedInUser = helper.extractLoggedInUser(request);

        return employerRepository.findByEmail(loggedInUser.getEmail())
                .orElseThrow(() -> new NotFoundException("Employer not found"));
    }


    @Override
    public Employer patchEmployer(String employerId, Map<Object, Object> fields) throws Exception {
            if (employerId == null) {
                throw new NotFoundException("Employer ID is required");}
            Optional<Employer> employer = employerRepository.findById(Long.valueOf(employerId));
            if (employer.isPresent()) {
                fields.forEach((key, value) -> {
                    Field field = ReflectionUtils.findField(Employer.class, String.valueOf(key));
                    if (field == null)
                        throw new IllegalArgumentException("Null fields cannot be updated! Kindly check the fields you are trying to update");

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

        employerRepository.delete(employer);
    }

    public EngagementForEmployer getEngagements(Long employerId, Pageable pageable) throws Exception {
        Employer employer = employerRepository.findById(employerId)
                .orElseThrow(() -> new NotFoundException("Employer was not found!"));

        Optional<List<JobPosting>> existingJobPostings = jobPostingRepository.findByEmployerID(String.valueOf(employerId));
        if(existingJobPostings.isEmpty()){
            throw new NotFoundException("Job Postings were not found!");
        };


        EngagementForEmployer engagements = new EngagementForEmployer();
        AtomicInteger applications = new AtomicInteger();
        AtomicInteger numberOfJobs = new AtomicInteger(existingJobPostings.get().size());
        AtomicInteger noOfApprovedJobs = new AtomicInteger();


        //FOR JOBS BY THE EMPLOYER WE FIND THE APPLICATIONS FOR EACH JOB. ADD THE SIZE OF THE APPLICATIONS TO THE ATOMIC INTEGER. WE HAVE NUMBER OF APPLICATIONS
        existingJobPostings.get().forEach((jobPosting -> {
            try{
            List<Application> applicationsForJob = applicationRepository.findByJobPosting(
                    mapper.map(jobPosting,JobPosting.class), pageable)
                    .orElseThrow(() -> new NotFoundException("Applications were not found!"));
            applications.addAndGet(applicationsForJob.size());
            // FOR EACH JOB WE FIND THE APPLICATIONS THAT ARE APPROVED. WE ADD THE SIZE OF THE APPROVED APPLICATIONS TO THE ATOMIC INTEGER. WE HAVE NUMBER OF APPROVED APPLICATIONS
                noOfApprovedJobs.addAndGet(applicationsForJob.stream()
                        .filter(x -> x.getApplicationStatus()
                                .equals(ApplicationStatus.APPROVED)).toList().size());}
            catch (Exception ex){
                log.error("Error occurred while fetching applications for job posting with ID: {}", jobPosting.getJobID());

            }
        }));
        
        engagements.setNoOfApplicants(applications);
        engagements.setNoOfJobPostings(numberOfJobs);
        engagements.setNoOfApprovedApplications(noOfApprovedJobs);
        return engagements;
    }


    @Override
    public List<JobPosting> getJobPostings(String employerId) throws Exception{
        Optional<List<JobPosting> >jobPosting =  jobPostingRepository.findByEmployerID(employerId);

        return jobPosting.orElseGet(ArrayList::new);



        }





}

