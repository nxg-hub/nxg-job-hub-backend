package core.nxg.entity;


import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;



@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "tech_talent")
public class TechTalentUser{

    @Id
    private String techId;

//    @Column(name="email")
    private String email;

    private String bio;

    private String highestQualification;


    private String experienceLevel;
    

    private String jobType;


    private String workMode;

    private String techTalentApprovingOfficer;

    private LocalDateTime techTalentDateOfApproval;


    private String professionalCert;
    
    private List<String> skills ;

//    ToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "user_id")
    @JsonIgnore
    @DBRef
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

   // private boolean isVerified;


//     public void addSkill(Skill<String> skill) {
//        skills.add(skill);
//        skill.setTechTalentUser(this);
//    }

    
}
