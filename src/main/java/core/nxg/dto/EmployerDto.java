package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class EmployerDto {
    private String employerID;
    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;
    private String country;
    private String industryType;
    private String companySize;
    private String rating;
}
