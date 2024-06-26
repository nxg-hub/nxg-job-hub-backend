package core.nxg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDTO {
   private Long applicationId;
   private Long jobPostingId;
   private String applicationStatus;
   private String timestamp;
    //applicant is the logged-in user:

    
}
