package core.nxg.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EngagementForEmployer {
    private int noOfApplicants;
    private int noOfApprovedApplications;

    private int noOfJobPostings;
}
