package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "comments")
public class Comments{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "comments_reactions_mapping",
    joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "reaction_id", referencedColumnName = "id"))
    @ToString.Exclude
    private List<Reactions> reactions;
}
