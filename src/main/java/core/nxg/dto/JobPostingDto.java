package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class JobPostingDto {
    private String jobID;
    private String employerID;
    private String title;
    private String description;
    private String salary;
    private String jobType;
    private String deadline;
    private String location;
    private String tags;
    private String comments;
    private String reactions;
}
