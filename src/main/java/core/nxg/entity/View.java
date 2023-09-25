package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
@Entity
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    private int viewCount;

    @OneToOne
    @PrimaryKeyJoinColumn
    private JobPosting jobPosting;

    
}
