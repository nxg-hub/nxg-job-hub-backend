package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "employer")
public class Employer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String employerID;
    private String companyName;
    private String companyDescription;
    private String position;
    private String companyAddress;
    private String country;
    private String industryType;
    private String companySize;
    private String rating;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Ratings> ratings;

}
