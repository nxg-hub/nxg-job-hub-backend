package core.nxg.repository;

import core.nxg.entity.NewEmployerUsers;
import core.nxg.entity.NewTalentUsers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NewEmployerUsersRepository extends MongoRepository<NewEmployerUsers, String> {
    List<NewEmployerUsers> findByDateJoinedAfter(LocalDateTime date);
    Page<NewEmployerUsers> findByDateJoinedAfter(LocalDateTime dateJoined, Pageable pageable);
}
