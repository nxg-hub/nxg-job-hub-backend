package core.nxg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsDto {
    private Long Id;
    private String comment;
    private String viewId;
    private String jobId;


}
