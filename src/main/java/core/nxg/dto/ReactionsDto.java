package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReactionsDto {
    private String reactionID;
    private String jobID;
    private String reactionType;
}
