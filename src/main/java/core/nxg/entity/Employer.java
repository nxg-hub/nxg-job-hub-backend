package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "employer")
public class Employer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String companyDescription; /// SHOULD WE HAVE A COMPANY ENTITY INSTEAD?
    private String position;
    private String companyAddress;
    private String companyWebsite; // <----- THAT CONTAINS THIS?
    private String country;
    private String industryType; //<------- AND THIS?
    
    @OneToOne
    private User user;

    private String companySize; /// <----- AND THIS? AFTER EVERYTHING IS DONE.

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Ratings> ratings;

}
