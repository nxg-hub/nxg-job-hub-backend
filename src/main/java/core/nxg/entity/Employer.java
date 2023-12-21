package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.enums.Rating;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "employer")
public class Employer {
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

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Ratings> ratings;

}
