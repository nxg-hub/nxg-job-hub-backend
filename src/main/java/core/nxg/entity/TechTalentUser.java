package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import core.nxg.enums.generic.Gender;
import core.nxg.enums.generic.JobType;
import core.nxg.enums.generic.Qualification;
import core.nxg.enums.generic.WorkMode;
import com.neovisionaries.i18n.CountryCode;

@Data
@RequiredArgsConstructor
@Entity
(name = "tech_talent")
public class TechTalentUser{

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
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @OneToOne
    @MapsId
    private User user;

    @Lob
    private byte[] resume;
    
    private CountryCode countryCode;

    private String Nationality;
    
    private String city;
    private String state;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String phoneNumber;
    private String professionalCert, currentJob;
    private int yearsOfExperience;
    private String skills;


    

    
}
