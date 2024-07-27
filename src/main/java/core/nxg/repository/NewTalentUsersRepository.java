package core.nxg.repository;

import core.nxg.entity.NewTalentUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewTalentUsersRepository extends MongoRepository<NewTalentUsers, String> {
    List<NewTalentUsers> findByDateJoinedAfter(LocalDateTime date);
    Page<NewTalentUsers> findByDateJoinedAfter(LocalDateTime dateJoined, Pageable pageable);
}
