package core.nxg.entity;

import core.nxg.enums.IndustryType;
import core.nxg.enums.JobType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@RequiredArgsConstructor
@Table(name = "agents")
public class TechTalentAgent{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
   // private String agentID;
    //private String gender;
    //private String userId;
    private JobType jobType;
    private IndustryType industryType;
    private String address;
    private String zipCode;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
