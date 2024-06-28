package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.nxg.enums.Rating;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Slf4j

@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "employer")
public class Employer {

    private static final double FRACTION_THRESHOLD = 0.75;

    @Id
    private Long employerID;

//    @Column(name="email")
    private String email;

    private String companyName;
    private String companyDescription;
    private String position;
    private String companyPhone;
    private String companyAddress;
    private String companyWebsite;
    private String country;

    private String industryType;

    private String jobBoard;
    
//    ToOne
//    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String companySize;

    private String CACCertificate;

    private String address;
    private String nationality;
    private String state;
    private String zipCode;
    private String companyZipCode;
    private String taxClearanceCertificate;
    private String TIN;
    private List<String> vacancies;
    private List<String> namesOfDirectors;

    private String companyMemorandum;

    private boolean verified;


    private List<Ratings> ratings;


    public boolean isVerified() {

        if (CACCertificate == null || taxClearanceCertificate == null || TIN == null || namesOfDirectors == null) {
            return false;
        }


        Field[] fields = this.getClass().getDeclaredFields();
        int totalFields = fields.length;
        int nonNullFields = 0;

        for (Field field : fields) {
            try {
                if (Optional.ofNullable(field.get(this) ).isPresent() ){
                    nonNullFields++;
                }
            } catch (IllegalAccessException e) {

                log.atLevel(Level.TRACE).log("Error accessing field: {}", field.getName(),e);
            }
        }


        double fraction = (double) nonNullFields / totalFields;

        return (fraction > FRACTION_THRESHOLD ) || verified;
    }
}
