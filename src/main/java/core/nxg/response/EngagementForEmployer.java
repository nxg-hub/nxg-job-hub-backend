package core.nxg.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@RequiredArgsConstructor
public class EngagementForEmployer {
    private AtomicInteger noOfApplicants;
    private AtomicInteger noOfApprovedApplications;

    private AtomicInteger noOfJobPostings;
}
