package core.nxg.entity;

import core.nxg.subscription.enums.JobStatus;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
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

    private LocalDate deadline;

    @Setter
    private LocalDate created_at = LocalDate.now();

    private String job_location;
    private List<String> tags;
    private List<Reactions> reactions;




    private List<Comments> comments;

    private boolean delivered;

    private View view;

    private boolean active;


    private JobStatus jobStatus;

}
