package core.nxg.service;

import org.springframework.data.domain.Pageable;

public interface AdminService {

     Object getAllTransactions(Pageable pageable);

     Object getAllEmployers(Pageable pageable);

    Object getAllJobs(Pageable pageable);

    void acceptJob(Long jobId);

    void rejectJob(Long jobId);

    void suspendUser(Long userId);

     void suspendJob(Long jobId);
}
