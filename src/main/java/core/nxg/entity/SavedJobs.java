package core.nxg.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "saved_jobs")
public class SavedJobs {
    @Id
    private Long id;

//    yToOne
    @JsonIgnore
    private User user;
//
//    yToOne
//    @JoinColumn(name = "jobPosting_id")
    private JobPosting jobPosting;
}
