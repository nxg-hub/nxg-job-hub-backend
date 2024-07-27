package core.nxg.repository;

import core.nxg.entity.NotVerifiedEmployers;
import core.nxg.entity.NotVerifiedTalents;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;

public interface NotVerifiedEmployersRepository extends MongoRepository<NotVerifiedEmployers, String> {
    Page<NotVerifiedEmployers> findByDateJoinedAfter(LocalDateTime dateAdded, Pageable pageable);
    Page<NotVerifiedEmployers> findAll(Pageable pageable);
}
