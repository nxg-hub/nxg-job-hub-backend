package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;




//import java.util.Locale.IsoCountryCode;
import core.nxg.enums.Experience;
import core.nxg.enums.JobType;
import core.nxg.enums.ProfessionalCert;
import core.nxg.enums.Qualification;
import core.nxg.enums.WorkMode;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Setter
@Getter
@RequiredArgsConstructor
@Entity
(name = "tech_talent")
public class TechTalentUser{

    @Id
    @Column(name = "techId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long techId;

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
    
    @OneToMany(mappedBy = "techTalentUser", cascade = CascadeType.PERSIST)
    private List<Skill<String>> skills ;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JsonIgnore
    private User user;

    private String resume;
    private String coverletter;
    private String linkedInUrl;
    private Locale countryCode;
    private String nationality;
    private String city;
    private String state;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String currentJob;
    private int yearsOfExperience;


     public void addSkill(Skill<String> skill) {
        skills.add(skill);
        skill.setTechTalentUser(this);
    }

    
}
