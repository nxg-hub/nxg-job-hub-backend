package core.nxg.repository;

import core.nxg.entity.TechTalentApprovalHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TechTalentApprovalHistoryRepository extends MongoRepository<TechTalentApprovalHistory, String> {

}
