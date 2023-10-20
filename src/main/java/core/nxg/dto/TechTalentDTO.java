package core.nxg.dto;

import lombok.*;
import core.nxg.enums.Experience;
import core.nxg.enums.JobType;
import core.nxg.enums.ProfessionalCert;
import core.nxg.enums.Qualification;
import core.nxg.enums.WorkMode;
import core.nxg.entity.Skill;
import core.nxg.entity.TechTalentUser;

import java.util.List;

//import java.util.List;
import java.util.Locale;

@Data
@RequiredArgsConstructor
public class TechTalentDTO {

    private String email;
    private List<Skill<String>> skills;
    private Qualification highestQualification;
    private Experience experienceLevel;
    private JobType jobType;
    private WorkMode workMode;
    private Locale countryCode;
    private String resume;
    private String nationality;
    private String coverletter;
    private String state;
    private ProfessionalCert professionalCert;    
    private String linkedInUrl;
    private String residentialAddress;
    private String city;
    private String zipCode;
    private String location;
    private String currentJob;
    private int yearsOfExperience;

    public TechTalentDTO(TechTalentUser user) {
        this.skills = user.getSkills();
        this.highestQualification = user.getHighestQualification();
        this.experienceLevel = user.getExperienceLevel();
        this.jobType = user.getJobType();
        this.workMode = user.getWorkMode();
        this.countryCode = user.getCountryCode();
        this.resume = user.getResume();
        this.nationality = user.getNationality();
        this.coverletter = user.getCoverletter();
        this.state = user.getState();
        this.professionalCert = user.getProfessionalCert();
        this.linkedInUrl = user.getLinkedInUrl();
        this.residentialAddress = user.getResidentialAddress();
        this.city = user.getCity();
        this.zipCode = user.getZipCode();
        this.location = user.getLocation();
        this.currentJob = user.getCurrentJob();
        this.yearsOfExperience = user.getYearsOfExperience();
        
    }


    


    
}
