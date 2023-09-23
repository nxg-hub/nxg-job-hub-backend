package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    private String skillName;

    @ManyToOne
    @JoinColumn(name = "tech_id")
    private TechTalentUser techTalentUser;

    
}
