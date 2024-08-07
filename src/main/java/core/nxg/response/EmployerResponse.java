package core.nxg.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployerResponse {
    private String employerID;
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
}
