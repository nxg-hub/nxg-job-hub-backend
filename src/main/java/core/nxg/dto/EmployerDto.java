package core.nxg.dto;

import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import core.nxg.entity.User;

@Getter
@Setter
public class EmployerDto {
    Long id;
    //private Long employerId;
    //private String email;
    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;
    private String companyWebsite;
    private String country;
    private String industryType;
    private String companySize;
    private String ratings;

    @OneToOne(mappedBy = "employer")
    private User user;

    }
   

