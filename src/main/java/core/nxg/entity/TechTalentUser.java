package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;




import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Setter
@Getter
@RequiredArgsConstructor
@Entity
(name = "tech_talent")
public class TechTalentUser{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long techId;

    @Column(name="email")
    private String email;

    private String bio;

    private String highestQualification;


    private String experienceLevel;
    

    private String jobType;


    private String workMode;


    private String professionalCert;
    
    private List<String> skills ;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String resume;
    private String coverletter;
    private String linkedInUrl;
    private String countryCode;
    private String city;
    private String state;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String currentJob;
    private int yearsOfExperience;
    private String profilePicture;
    private String portfolioLink;
    private String jobInterest;

    private boolean isVerified;


//     public void addSkill(Skill<String> skill) {
//        skills.add(skill);
//        skill.setTechTalentUser(this);
//    }

    
}
