package core.nxg.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import core.nxg.entity.TechTalentUser;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ApplicationDTO {

   private Long applicationId;
   private String applicationStatus;
   private Date timestamp;
   private TechTalentUser applicant;

    
}
