package core.nxg.serviceImpl;

import core.nxg.entity.*;
import core.nxg.repository.*;
import core.nxg.response.EmployerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployerManagementService {

    @Autowired
    private EmployerRepository employerRepository;
    @Autowired
    private  NewEmployerUsersRepository newEmployerUsersRepository;
    @Autowired
    private NotVerifiedEmployersRepository notVerifiedEmployersRepository;

    // This method runs every Monday at midnight to add new talents
    @Scheduled(cron = "0 0 0 * * MON")
    public void processNewTalents() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<Employer> newEmployers = employerRepository.findByAccountCreationDateAfter(oneWeekAgo);

        for (Employer employer : newEmployers) {
            NewEmployerUsers newEmp = new NewEmployerUsers();
            newEmp.setEmail(newEmp.getEmail());
            newEmp.setIndustryType(newEmp.getIndustryType());
            newEmployerUsersRepository.save(newEmp);
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void processVerificationStatus() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<NewEmployerUsers> newEmployerUsers = newEmployerUsersRepository.findAll();

        for (NewEmployerUsers newEmployerUser : newEmployerUsers) {
            if (newEmployerUser.getDateJoined().isBefore(oneWeekAgo)) {
                Optional<EmployerResponse> employerResponse = employerRepository.findByEmail(newEmployerUser.getEmail());
                if (employerResponse.isPresent()) {
                    Employer employer = mapEmployerResponseToEmployer(employerResponse.get());
                    if (employer.isVerified()) {
                        newEmployerUsersRepository.delete(newEmployerUser);
                    } else {
                        NotVerifiedEmployers notVerifiedEmployer = new NotVerifiedEmployers();
                        notVerifiedEmployer.setEmail(newEmployerUser.getEmail());
                        notVerifiedEmployer.setIndustryType(newEmployerUser.getIndustryType());
                        notVerifiedEmployersRepository.save(notVerifiedEmployer);
                        newEmployerUsersRepository.delete(newEmployerUser);
                    }
                }
            }
        }
    }


    public Page<NewEmployerUsers> getNewEmployersFromLastWeek(int page, int size) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return newEmployerUsersRepository.findByDateJoinedAfter(oneWeekAgo, PageRequest.of(page, size));
    }

    public Page<NotVerifiedEmployers> getAllNotVerifiedEmployers(int page, int size) {
        return notVerifiedEmployersRepository.findAll(PageRequest.of(page, size));
    }

    private Employer mapEmployerResponseToEmployer(EmployerResponse employerResponse) {
        Employer employer = new Employer();
        employer.setEmployerID(employerResponse.getEmployerID());
        employer.setEmail(employer.getEmail());
        employer.setIndustryType(employerResponse.getIndustryType());
        employer.setAccountCreationDate(employer.getAccountCreationDate());
        return employer;
    }
}
