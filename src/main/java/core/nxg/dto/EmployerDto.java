package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
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
