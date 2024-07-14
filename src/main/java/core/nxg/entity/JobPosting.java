package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.subscription.enums.JobStatus;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "jobPosting")
public class JobPosting {
    @Id
    private String jobID;

    private String employerID;

    private String job_title;
    private String job_description;
    private String salary;
    private String company_bio;
    private String requirements;
    private String job_type;
    private String employer_name;
    private String employer_profile_pic;

    private LocalDate deadline;

    @JsonIgnore
    @DBRef
    private Employer employer;

    @Setter
    private LocalDateTime createdAt = LocalDateTime.now();

    private String job_location;
    private List<String> tags;
    private List<Reactions> reactions;




    private List<Comments> comments;

    private boolean delivered;

    private View view;

    private boolean active;


    private JobStatus jobStatus;

}
