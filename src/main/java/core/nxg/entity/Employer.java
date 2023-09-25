package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Data
@Table(name = "employer")
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;
    private String country;
    private String industryType;
    private String companySize;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Ratings> ratings;

}
