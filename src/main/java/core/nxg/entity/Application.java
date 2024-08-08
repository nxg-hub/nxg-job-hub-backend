package core.nxg.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import core.nxg.enums.ApplicationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "applications")
public class Application {

    @Id
    private String applicationId;


    private LocalDateTime timestamp;

    private ApplicationStatus applicationStatus;

    @JsonIgnore
    private User applicant;

    private int matchingScore;

    private JobPosting jobPosting;

    // Method to calculate matching score
    public void calculateMatchingScore() {
        TechTalentUser techTalent = applicant.getTechTalent();
        if (techTalent == null || jobPosting == null) {
            this.matchingScore = 0;
            return;
        }

        // Extract keywords from job description and requirements
        String jobDescription = jobPosting.getJob_description().toLowerCase();
        String jobRequirements = jobPosting.getRequirements().toLowerCase();

        // Score for skills matching
        int skillMatchScore = calculateSkillsMatch(techTalent.getSkills(), jobDescription, jobRequirements);

        // Score for experience level
        int experienceLevelScore = calculateExperienceLevelMatch(techTalent.getExperienceLevel(), jobPosting.getJob_type());

        // Score for years of experience
        int yearsOfExperienceScore = calculateYearsOfExperienceMatch(techTalent.getYearsOfExperience(), jobRequirements);

        // Combine scores to calculate the final matching score (adjust weights as needed)
        this.matchingScore = (int) (0.5 * skillMatchScore + 0.3 * experienceLevelScore + 0.2 * yearsOfExperienceScore);
    }

    // Calculate the skill match score
    private int calculateSkillsMatch(List<String> skills, String jobDescription, String jobRequirements) {
        int matchCount = 0;
        for (String skill : skills) {
            if (jobDescription.contains(skill.toLowerCase()) || jobRequirements.contains(skill.toLowerCase())) {
                matchCount++;
            }
        }
        return (int) ((double) matchCount / skills.size() * 100);
    }

    // Calculate the experience level score
    private int calculateExperienceLevelMatch(String experienceLevel, String jobType) {
        if (experienceLevel == null || jobType == null) {
            return 0;
        }
        return experienceLevel.equalsIgnoreCase(jobType) ? 100 : 0;
    }

    // Calculate the years of experience score
    private int calculateYearsOfExperienceMatch(int yearsOfExperience, String jobRequirements) {
        // Extract required years of experience from job requirements (simple heuristic)
        String regex = "(\\d+)\\s+years?\\s+of\\s+experience";
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile(regex).matcher(jobRequirements);
        int requiredYears = 0;
        while (matcher.find()) {
            requiredYears = Integer.parseInt(matcher.group(1));
        }
        if (requiredYears <= yearsOfExperience) {
            return 100;
        } else {
            return (int) ((double) yearsOfExperience / requiredYears * 100);
        }
    }
}
