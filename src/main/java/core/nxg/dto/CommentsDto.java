package core.nxg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsDto {
    private String id;
    private String comment;
    private String viewID;
    private String jobID;
    private String commenterID;
}
