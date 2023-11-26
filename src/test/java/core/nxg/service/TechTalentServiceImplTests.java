package core.nxg.service;

import org.junit.jupiter.api.Test;
import java.util.Locale;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//import java.util.List;


//import core.nxg.entity.Skill;
import core.nxg.entity.TechTalentUser;
import core.nxg.enums.Experience;
import core.nxg.enums.JobType;
import core.nxg.enums.ProfessionalCert;
import core.nxg.enums.Qualification;
import core.nxg.enums.WorkMode;
import core.nxg.repository.TechTalentRepository;
public class TechTalentServiceImplTests {

    private TechTalentRepository techTalentRepository;

    
    @Test
    private void testcreateTechTalentUser() {
        TechTalentUser techTalentUser = new TechTalentUser();
        techTalentUser.setHighestQualification(Qualification.MSC);
        techTalentUser.setExperienceLevel(Experience.MID);
        techTalentUser.setJobType(JobType.FULL_TIME);
        techTalentUser.setWorkMode(WorkMode.ONSITE);
        techTalentUser.setProfessionalCert(ProfessionalCert.PSO);
        techTalentUser.setResume("/path/to/resume.pdf");
        techTalentUser.setCoverletter("/path/to/coverletter.pdf");
        techTalentUser.setLinkedInUrl("https://www.linkedin.com/in/john-doe");
        techTalentUser.setCountryCode(Locale.US.getCountry());
        techTalentUser.setCity("San Francisco");
        techTalentUser.setState("CA");
        techTalentUser.setResidentialAddress("123 Main Street");
        techTalentUser.setZipCode("94105");
        techTalentUser.setLocation("San Francisco, CA");
        techTalentUser.setCurrentJob("Software Engineer");
        techTalentUser.setYearsOfExperience(5);
        techTalentRepository.save(techTalentUser);
        

        assertNotNull(techTalentUser);
    }

   

}
