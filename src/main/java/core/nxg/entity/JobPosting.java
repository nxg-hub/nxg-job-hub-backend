package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "jobPosting")
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long jobID;

    private Long employerID;
    private String job_title;
    private String job_description;
    private String salary;
    private String company_bio;
    private String requirements;
    private String job_type;
    @Temporal(TemporalType.DATE)
    private LocalDate deadline;

    @Temporal(TemporalType.DATE)
    @Getter
    private LocalDate created_at;

    public void SetCreatedAt(){
        this.created_at = LocalDate.now();
    }

    private String location;
    private String tags;
    private String reaction;



    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reactions> reactions;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Comments> comments;

    @OneToOne
    private View view;

}
