package core.nxg.entity;

import core.nxg.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@Entity
@Table(name = "reaction")
@RequiredArgsConstructor
public class Reactions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType reactionType;


}
