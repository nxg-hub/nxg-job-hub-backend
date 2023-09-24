package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@RequiredArgsConstructor
public class View {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    private int viewCount;


////////////////////////////////////////////////
 //   @ManyToOne
    //@PrimaryKeyJoinColumn
   // private JobPosting jobPosting;
//////////////////////////////////////////////////
    
}
