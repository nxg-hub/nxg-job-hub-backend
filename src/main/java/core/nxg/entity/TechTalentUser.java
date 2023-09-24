package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;




import java.util.Locale.IsoCountryCode;
import core.nxg.enums.Experience;
import core.nxg.enums.JobType;
import core.nxg.enums.ProfessionalCert;
import core.nxg.enums.Qualification;
import core.nxg.enums.WorkMode;
import core.nxg.entity.Skill;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
(name = "tech_talent")
public class TechTalentUser{

 /**
     * A tech talent type of User.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tech_id;

    @Enumerated(EnumType.STRING)
    private Qualification highestQualification;

    @Enumerated(EnumType.STRING)
    private Experience experienceLevel;
    
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    @Enumerated(EnumType.STRING)
    private ProfessionalCert professionalCert;

    @OneToMany
    private Skill skills ;


    @OneToOne
    @MapsId
    private User user;

    private String resume;
    private String coverletter;
    private String linkedInUrl;
    private IsoCountryCode countryCode;
    private String Nationality;
    private String city;
    private String state;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String currentJob;
    private int yearsOfExperience;


    

    
}
