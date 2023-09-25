package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CommentsDto {
    private Long id;
    private String viewID;
    private String jobID;
}
