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

    @Column(name = "skill_name")
    private String skillName; //if skill ! in available skill add skill to available skill
   
    private List<String> availableSkills = Arrays.asList(
            "Java",
            "Python",
            "C Sharp"
    );
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "techId")
    private TechTalentUser techTalentUser;
    
    public Skill(String skillName, ArrayList<E> availableSkills){
        this.skillName = skillName;
        this.availableSkills = new ArrayList<>();
        this.addSkillIfNotExists(skillName);

    }
    public void addSkillIfNotExists(String skillName) {
        if (!availableSkills.contains(skillName)) {
            availableSkills.add(skillName);
        }
    }
    
    public Skill<E> addAllSkillsIfNotExist(List<Skill<E>> skills) {
        for (Skill<?> skill : skills) {
            addSkillIfNotExists((String) skill.getSkillName());
        }
        return this;
    }
} 
