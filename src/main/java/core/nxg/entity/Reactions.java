package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "reaction")
@RequiredArgsConstructor
public class Reactions extends JobPosting{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reactionID;
    private String reactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;
}
