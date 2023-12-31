package core.nxg.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class TechTalentResponse {
    private Long techId;
    private List<String> skills;
    private String highestQualification;
    private String experienceLevel;
    private String jobType;
    private String workMode;
    private String countryCode;
    private String resume;
    private String coverletter;
    private String state;
    private String professionalCert;
    private String linkedInUrl;
    private String residentialAddress;
    private String city;
    private String zipCode;
    private String location;
    private String currentJob;
    private int yearsOfExperience;
}
