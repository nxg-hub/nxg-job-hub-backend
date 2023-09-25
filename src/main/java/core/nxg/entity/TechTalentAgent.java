package core.nxg.entity;

import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@RequiredArgsConstructor
@Table(name = "agents")
public class TechTalentAgent extends User {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String agentID;
    private String gender;
    private String jobType;
    private String industryType;
    private String address;
    private String zipCode;

}
