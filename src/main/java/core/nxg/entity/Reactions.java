package core.nxg.entity;

import core.nxg.enums.ReactionType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;


@Data
@Entity
@Table(name = "reaction")
@RequiredArgsConstructor
public class Reactions{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private ReactionType reactionType;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<JobPosting> jobPosting;

    @ManyToMany
    private List<Comments> comments;
}
