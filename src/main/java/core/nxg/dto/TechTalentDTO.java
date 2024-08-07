package core.nxg.dto;

import io.jsonwebtoken.io.SerializationException;
import io.jsonwebtoken.io.Serializer;
import lombok.*;
import core.nxg.enums.Experience;
import core.nxg.enums.JobType;
import core.nxg.enums.ProfessionalCert;
import core.nxg.enums.Qualification;
import core.nxg.enums.WorkMode;
import core.nxg.entity.Skill;
import core.nxg.entity.TechTalentUser;

import java.io.Serializable;
import java.util.List;

//import java.util.List;
import java.util.Locale;
import java.util.Set;


@Data
@AllArgsConstructor
@Getter
@Setter
@RequiredArgsConstructor
public class TechTalentDTO implements Serializable {
    private String techId;
    private List<String> skills;
    private String bio;
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
    private String profilePicture;
    private String portfolioLink;
    private String jobInterest;
    private boolean verified;



}
