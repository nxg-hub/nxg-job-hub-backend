package core.nxg.serviceImpl;

import core.nxg.entity.NewTalentUsers;
import core.nxg.entity.NotVerifiedTalents;
import core.nxg.entity.TechTalentUser;
import core.nxg.repository.NewTalentUsersRepository;
import core.nxg.repository.NotVerifiedTalentsRepository;
import core.nxg.repository.TechTalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TalentManagementService {

    @Autowired
    private TechTalentRepository techTalentRepository;
    @Autowired
    private NewTalentUsersRepository newTalentUsersRepository;
    @Autowired
    private NotVerifiedTalentsRepository notVerifiedTalentsRepository;

    // This method runs every Monday at midnight to add new talents
    @Scheduled(cron = "0 0 0 * * MON")
    public void processNewTalents() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<TechTalentUser> newTalents = techTalentRepository.findByAccountCreationDateAfter(oneWeekAgo);

        for (TechTalentUser talent : newTalents) {
            NewTalentUsers newTalent = new NewTalentUsers();
            newTalent.setEmail(talent.getEmail());
            newTalent.setJobInterest(talent.getJobInterest());
            newTalentUsersRepository.save(newTalent);
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void processVerificationStatus() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        List<NewTalentUsers> newTalents = newTalentUsersRepository.findAll();

        for (NewTalentUsers newTalent : newTalents) {
            // Check if the talent joined more than a week ago
            if (newTalent.getDateJoined().isBefore(oneWeekAgo)) {
                Optional<TechTalentUser> techTalent = techTalentRepository.findByEmail(newTalent.getEmail());
                if (techTalent.isPresent() && techTalent.get().isVerified()) {
                    newTalentUsersRepository.delete(newTalent);
                } else {
                    NotVerifiedTalents notVerifiedTalent = new NotVerifiedTalents();
                    notVerifiedTalent.setEmail(newTalent.getEmail());
                    notVerifiedTalent.setJobInterest(newTalent.getJobInterest());
                    notVerifiedTalentsRepository.save(notVerifiedTalent);
                    newTalentUsersRepository.delete(newTalent);
                }
            }
        }
    }

    public Page<NewTalentUsers> getNewTalentsFromLastWeek(int page, int size) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return newTalentUsersRepository.findByDateJoinedAfter(oneWeekAgo, PageRequest.of(page, size));
    }

    public Page<NotVerifiedTalents> getAllNotVerifiedTalents(int page, int size) {
        return notVerifiedTalentsRepository.findAll(PageRequest.of(page, size));
    }
}
