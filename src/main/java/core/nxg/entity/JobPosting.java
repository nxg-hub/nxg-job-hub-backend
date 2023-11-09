package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "jobPosting")
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
// <<<<<<< controllers-update
    private Long jobId;
    private String employerId;
// =======
//     private Long id;
//     private String jobID;

//     private String employerID;
// >>>>>>> main
    private String title;
    private String description;
    private String salary;
    private String jobType;
    private String deadline;
    private String location;
    private String tags;
    private String reaction;

    @OneToMany(mappedBy = "jobPosting", cascade = CascadeType.ALL)
    private List<Comments> comments;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(name = "reactions_jobPosting_mapping",
            joinColumns = @JoinColumn(name = "job_posting_id", referencedColumnName = "jobId"),
            inverseJoinColumns = @JoinColumn(name = "reaction_id", referencedColumnName = "id"))
    @ToString.Exclude
    private List<Reactions> reactions = new ArrayList<>();
}
