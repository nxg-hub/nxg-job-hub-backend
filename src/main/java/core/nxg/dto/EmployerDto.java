package core.nxg.dto;

import core.nxg.entity.Ratings;
import core.nxg.enums.Rating;
import jakarta.persistence.OneToOne;
import lombok.*;
import core.nxg.entity.User;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class EmployerDto {

    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;

    private String companyPhone;
    private String companyWebsite;
    private String country;
    private String industryType;
    private String companySize;

    private String jobBoard;

    private String CACCertificate;

    private String taxClearanceCertificate;
    private String TIN;

    private List<String> namesOfDirectors;

    private String companyMemorandum;



}
   

