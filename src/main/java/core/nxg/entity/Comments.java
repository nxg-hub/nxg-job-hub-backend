package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@RequiredArgsConstructor
@Table(name = "comments")
public class Comments extends JobPosting{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String jobID;

    @ManyToOne(fetch = FetchType.LAZY)
    private JobPosting jobPosting;
}
