package core.nxg.entity;


import jakarta.persistence.*;
import lombok.*;
import core.nxg.enums.ApplicationStatus;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column(name = "application_date")
    private Date timestamp;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus applicationStatus;


    @ManyToOne
    @JoinColumn(name = "applicant_id", referencedColumnName = "id")
    private TechTalentUser applicant;

////////////////////////////////////////////////////////////////////
    //@ManyToOne
    //@JPrimaryKeyJoinColumn
    //private JobPosting jobPosting;
///////////////////////////////////////////////////////////////// 
}
