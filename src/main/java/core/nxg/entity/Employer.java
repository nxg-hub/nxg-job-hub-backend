package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.nxg.enums.Rating;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "employer")
public class Employer {

    private static final double FRACTION_THRESHOLD = 0.75;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long employerID;

    @Column(name="email")
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
    
    @OneToOne
    @JoinColumn(name = "user_id")
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



    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Ratings> ratings;

    @JsonProperty("isVerified")
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
                e.printStackTrace();
                log.atLevel(Level.TRACE).log("Error accessing field: {}", field.getName(),e);
            }
        }


        double fraction = (double) nonNullFields / totalFields;

        return fraction > FRACTION_THRESHOLD;
    }
}
