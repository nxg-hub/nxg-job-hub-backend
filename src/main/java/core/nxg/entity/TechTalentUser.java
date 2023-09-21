package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@Entity
(name = "tech_talent")
public class TechTalentUser{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tech_id;
    private String Nationality;
    private String CountryCode;
    private String City;
    private String State;
    private String residentialAddress;
    private String zipCode;
    private String location;
    private String phoneNumber;
    private String highestQualification, professionalCert, currentJob;
    private String yearsOfExperience;
    private String skills, jobType, workMode;

    @OneToOne
    @MapsId
    private User user;

    @Lob
    private byte[] resume;

    
}
