package core.nxg.repository;

import core.nxg.entity.EmployerApprovalHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployerApprovalHistoryRepository extends MongoRepository<EmployerApprovalHistory, String> {

}
