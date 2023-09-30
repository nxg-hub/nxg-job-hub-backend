package core.nxg.dto;

import lombok.*;
import core.nxg.enums.Experience;
import core.nxg.enums.JobType;
import core.nxg.enums.ProfessionalCert;
import core.nxg.enums.Qualification;
import core.nxg.enums.WorkMode;
import core.nxg.entity.Skill;
import java.util.List;

//import java.util.List;
import java.util.Locale;

@Getter
@Setter
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
    private byte coverletter;
    private ProfessionalCert professionalCert;    
    private String linkedInUrl;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String currentJob;
    private int yearsOfExperience;

    
}
