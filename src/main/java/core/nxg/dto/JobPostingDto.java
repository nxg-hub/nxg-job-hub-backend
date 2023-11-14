package core.nxg.dto;

import core.nxg.entity.Reactions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JobPostingDto {
    private Long jobId;
    private String employerId;
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
