package core.nxg.dto;

import core.nxg.enums.ReactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReactionsDto {
    //private String reactionID;
    //private String jobID;
    private ReactionType reactionType;
}
