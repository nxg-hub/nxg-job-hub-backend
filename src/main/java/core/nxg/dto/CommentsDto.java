package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class CommentsDto {
    private Long id;
    private String viewID;
    private String jobID;
}
