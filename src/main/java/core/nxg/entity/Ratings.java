package core.nxg.entity;

import core.nxg.enums.Rating;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "ratings")
public class Ratings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Rating rating;

    @ManyToOne()
    @JoinColumn(name = "employer_id")
    private Employer employer;

}
