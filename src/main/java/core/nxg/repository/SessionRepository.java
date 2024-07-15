package core.nxg.repository;

import core.nxg.entity.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    Optional<Session> findByUserId(String userId);

    @Query(value = "{ 'isActive' : true }", count = true)
    long countActiveSessions();

}