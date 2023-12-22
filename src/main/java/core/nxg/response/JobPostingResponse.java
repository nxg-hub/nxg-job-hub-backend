package core.nxg.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class JobPostingResponse {
    private Long jobID;
    private String title;
    private String job_description;
    private String company_bio;
    private String salary;
    private String job_type;
    private LocalDate deadline;
    private LocalDate created_at;
    private String location;
    private String tags;
    private String comments;
    private String reactions;
}

