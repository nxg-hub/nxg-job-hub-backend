package core.nxg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentsDto {
    private Long id;
    private String comment;
    private Long viewID;
    private Long jobID;
    private Long commenterID;
}
