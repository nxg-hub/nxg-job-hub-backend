package core.nxg.entity;



import lombok.*;
import core.nxg.enums.ApplicationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "applications")
public class Application {

    @Id
    private String applicationId;


    private LocalDateTime timestamp;

    private ApplicationStatus applicationStatus;


    private User applicant;

    private JobPosting jobPosting;
}
