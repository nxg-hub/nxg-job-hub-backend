package core.nxg.response;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobPostingResponse {

    private Long jobID;
    private String employerID;
    private String job_title;
    private String job_description;
    private String company_bio;
    private String salary;
    private String job_type;
    private String requirements;
    private LocalDate deadline;
    private LocalDate created_at;
    private String location;
    private String tags;
    private String comments;
    private String reactions;
}

