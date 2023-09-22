package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import core.nxg.enums.generic.Experience;
import com.fasterxml.jackson.annotation.JsonFormat;
import core.nxg.enums.generic.Gender;
import core.nxg.enums.generic.JobType;
import core.nxg.enums.generic.Qualification;
import core.nxg.enums.generic.WorkMode;
import core.nxg.enums.generic.professionalCert;
import java.util.List;

import com.neovisionaries.i18n.CountryCode;
@Setter
@Getter
@RequiredArgsConstructor
@Entity
(name = "tech_talent")
public class TechTalentUser extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @OneToOne
    @MapsId
    private User user;

    @Lob
    private byte[] resume;

    @Lob
    private byte coverletter;

    @Enumerated(EnumType.STRING)
    private professionalCert professionalCert;


    private String linkedInUrl;
    
    private CountryCode countryCode;

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
