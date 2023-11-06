package core.nxg.dto;

import lombok.Getter;
import core.nxg.entity.TechTalentUser;
import core.nxg.enums.ApplicationStatus;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationDTO {
   private Long applicationId;
   private Long jobPostingId;
   private ApplicationStatus applicationStatus;
   private LocalDateTime timestamp;
   // private TechTalentUser applicant;

    
}
