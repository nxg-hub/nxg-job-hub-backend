package core.nxg.entity;


import jakarta.persistence.*;
import lombok.*;
import core.nxg.enums.ApplicationStatus;

import java.time.LocalDateTime;
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
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicantid", referencedColumnName = "tech_id")
    private TechTalentUser applicant;

////////////////////////////////////////////////////////////////////
    //@ManyToOne
    //@JPrimaryKeyJoinColumn
    //private JobPosting jobPosting;
///////////////////////////////////////////////////////////////// 
}
