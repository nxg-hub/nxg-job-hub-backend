package core.nxg.entity;

import core.nxg.enums.ReactionType;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "reaction")
@RequiredArgsConstructor
public class Reactions{
    @Id
    private String id;

    private ReactionType reactionType;


}
