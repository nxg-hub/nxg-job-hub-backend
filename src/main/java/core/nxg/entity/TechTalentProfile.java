package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name="techTalentProfile")
public class TechTalentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String countryCode;
    private String currentLocation;
    private String residentialAddress;
    private String highestQualification;
    private String yearsOfExperience;
    private String certifications;
    private String jobFunction;

}

