package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TechTalentProfileDto {

    private String fullName;
    private String email;
    private String mobileNumber;
    private String countryCode;
    private String currentLocation;
    private String residentialAddress;
    private String highestQualification;
    private String yearsOfExperience;
    private List<String> certifications;
    private String jobFunction;
    private List<String> jobInterests;
    private String experienceLevel;
    private String jobType;
    private String workMode;
    private String passportPhotograph; // File path or URL
    private String resumeCV; // File path or URL
    private String coverLetter;
    private String projectPortfolioLink;
    private String linkedinProfileLink;
    private String bio;

    // Constructors, getters, setters, and other methods
}
