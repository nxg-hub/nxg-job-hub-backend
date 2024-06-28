package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "views")
public class View {

    @Id
    private String viewId;

    private int viewCount;

//    yToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "job_postings_views_mapping",
//            joinColumns = @JoinColumn(name = "view_id", referencedColumnName = "viewId"),
//            inverseJoinColumns = @JoinColumn(name = "job_posting_id", referencedColumnName = "jobId"))
//    @ToString.Exclude
    @JsonIgnore
    private List<JobPosting> jobPostings;

    
}
