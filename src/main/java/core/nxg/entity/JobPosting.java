package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "jobPosting")
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobID;

    @Column(name = "employer_id")
    private String employerID;

    private String job_title;
    private String job_description;
    private String salary;
    private String company_bio;
    private String requirements;
    private String job_type;
    @Temporal(TemporalType.DATE)
    private LocalDate deadline;

    @Setter
    @Temporal(TemporalType.DATE)
    private LocalDate created_at = LocalDate.now();

    private String job_location;
    private String tags;
    private String reaction;



    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reactions> reactions;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comments> comments;

    @OneToOne
    private View view;

}
