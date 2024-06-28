package core.nxg.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "comments")
public class Comments{
    @Id
    private Long id;
    private String comment;

//    yToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "job_posting_id")
    private JobPosting jobPosting;

//    yToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "comments_reactions_mapping",
//    joinColumns = @JoinColumn(name = "comment_id", referencedColumnName = "id"),
//    inverseJoinColumns = @JoinColumn(name = "reaction_id", referencedColumnName = "id"))
//    @ToString.Exclude
    private List<Reactions> reactions;
}
