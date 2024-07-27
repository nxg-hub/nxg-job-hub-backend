package core.nxg.repository;

import core.nxg.entity.NotVerifiedTalents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface NotVerifiedTalentsRepository extends MongoRepository<NotVerifiedTalents, String> {
    Page<NotVerifiedTalents> findByDateJoinedAfter(LocalDateTime dateAdded, Pageable pageable);
    Page<NotVerifiedTalents> findAll(Pageable pageable);
}
