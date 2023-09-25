package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ReactionsDto {
    private String reactionID;
    private String jobID;
    private String reactionType;
}
