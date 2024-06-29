package core.nxg.repository;

import core.nxg.entity.Reactions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReactionsRepository extends MongoRepository<Reactions, String> {
}

