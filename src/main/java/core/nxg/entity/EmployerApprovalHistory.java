package core.nxg.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import core.nxg.enums.ApprovalType;
import core.nxg.enums.UserType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Getter
@Setter
@Document(collection = "employer-approval-history")
public class EmployerApprovalHistory {

    @Id
    private String id;
    private String employerId;
    private String jobId;
    private String jobTitle;
    private String employerName;
    private UserType userType;
    private ApprovalType approvalType;
    private String approvalOfficerName;
    private LocalDateTime dateOfApproval;
    private String disapprovalReason;
    private LocalDateTime dateOfDisapproval;
    private LocalDateTime dateOfJobSuspension;
    private String reasonForJobSuspension;
    private LocalDateTime dateOfProfileSuspension;
    private String reasonForProfileSuspension;
    private LocalDateTime profileVerificationRejectionDate;
    private String profileVerificationRejectionReason;


    @JsonIgnore
    @DBRef
    private Employer employer;

}
