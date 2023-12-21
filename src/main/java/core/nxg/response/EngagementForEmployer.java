package core.nxg.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class EngagementForEmployer {
    private int noOfApplicants;
    private int noOfApprovedApplications;

    private int noOfJobPostings;
}
