package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.Date;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.neovisionaries.i18n.CountryCode;
import core.nxg.enums.generic.Experience;
import core.nxg.enums.generic.Qualification;
import core.nxg.enums.generic.JobType;
import core.nxg.enums.generic.WorkMode;
import core.nxg.enums.generic.professionalCert;
import core.nxg.enums.generic.Gender;
import core.nxg.enums.generic.MaritalStaus;
import java.util.List;

@Data
@RequiredArgsConstructor
public class TechTalentDto {

    private Long tech_id;

    @JsonFormat(pattern = "yyyy-MM-dd") 
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Qualification highestQualification;

    @Enumerated(EnumType.STRING)
    private Experience experienceLevel;
    
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    private MaritalStaus maritalStatus;

    private CountryCode countryCode;

    @Lob
    private byte[] resume;

    @Lob
    private byte coverletter;

    @Enumerated(EnumType.STRING)
    private professionalCert professionalCert;

    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String linkedInUrl;
    private String Nationality;
    private String city;
    private String state;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String phoneNumber;
    private String currentJob;
    private int yearsOfExperience;
    private List<String> skills;

    
}
