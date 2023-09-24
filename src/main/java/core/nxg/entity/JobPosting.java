package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
@Table(name = "jobPosting")
public class JobPosting {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobID;
    private String employerID;
    private String title;
    private String description;
    private String salary;
    private String jobType;
    private String deadline;
    private String location;
    private String tags;
    private String comment;
    private String reaction;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Reactions> reactions;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comments> comments;
}
