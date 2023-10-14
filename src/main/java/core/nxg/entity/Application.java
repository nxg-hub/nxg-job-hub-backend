package core.nxg.entity;


import jakarta.persistence.*;
import lombok.*;
import core.nxg.enums.ApplicationStatus;

import java.time.LocalDateTime;

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
    @PrimaryKeyJoinColumn
    private TechTalentUser applicant;


    @ManyToOne
    @PrimaryKeyJoinColumn
    private JobPosting jobPosting;
}
