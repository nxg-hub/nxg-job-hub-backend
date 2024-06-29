package core.nxg.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "skills")
public class Skill<E> {
    @Id
    private Long id;


    private String skillName; //if skill ! in available skill add skill to available skill
   
    private List<String> availableSkills = Arrays.asList(
            "Java",
            "Python",
            "C Sharp"
    );

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


} 
