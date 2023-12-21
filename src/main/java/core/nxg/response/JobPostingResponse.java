package core.nxg.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JobPostingResponse {
    private Long jobID;
    private String employerID;
    private String title;
    private String job_description;
    private String company_bio;
    private String salary;
    private String job_type;
    private String deadline;
    private String location;
    private String tags;
    private String comments;
    private String reactions;
}

