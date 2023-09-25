package core.nxg.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.TechTalentUser;
import java.util.Date;

@Data
@RequiredArgsConstructor
public class ApplicationDTO {

   private Long applicationId;
   private String applicationStatus;
   private Date timestamp;
   private TechTalentUser applicant;

    
}
