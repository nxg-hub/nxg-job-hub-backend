package core.nxg.dto;

import core.nxg.entity.Ratings;
import core.nxg.enums.Rating;
import jakarta.persistence.OneToOne;
import lombok.*;
import core.nxg.entity.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class EmployerDto {
    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;
    private String companyWebsite;
    private String country;
    private String industryType;
    private String companySize;


    }
   

