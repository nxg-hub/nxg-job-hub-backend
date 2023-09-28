package core.nxg.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "skills")
public class Skill<E> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String skillName; //if skill ! in available skill add skill to available skill
   ;    
    private List<String> availableSkills = Arrays.asList(
            "Java",
            "Python",
            "C Sharp"
    );
    @ManyToOne
    @PrimaryKeyJoinColumn
    private TechTalentUser techTalentUser;
    
    public Skill(String skillName, ArrayList<E> availableSkills){
        this.skillName = skillName;
        this.availableSkills = new ArrayList<>();

    }
    public void addSkillIfNotExists(String skillName) {
        if (!availableSkills.contains(skillName)) {
            availableSkills.add(0, skillName);
        }
    }
    
}
