package core.nxg.entity;

import core.nxg.enums.ApplicationStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Document(collection = "jobApprovalHistory")
@Getter
@Setter
@RequiredArgsConstructor
public class JobApplicationHistory {

        @Id
        private String id;
        private String jobId;
        private String techTalentId;
        private String techTalentName;
        private String adminId;
        private String employerId;
        private String employerName;
        private ApplicationStatus approvalStatus;
        private LocalDateTime timestamp;
        private String comments;

    }
