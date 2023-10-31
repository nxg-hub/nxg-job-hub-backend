package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    private int viewCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "job_postings_views_mapping",
            joinColumns = @JoinColumn(name = "view_id", referencedColumnName = "viewId"),
            inverseJoinColumns = @JoinColumn(name = "job_posting_id", referencedColumnName = "jobId"))
    @ToString.Exclude
    private List<JobPosting> jobPostings;

    
}
