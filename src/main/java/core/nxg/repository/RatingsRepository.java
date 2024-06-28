package core.nxg.repository;

import core.nxg.entity.Employer;
import core.nxg.entity.Ratings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RatingsRepository extends MongoRepository<Ratings, String> {
    List<Ratings> findAllByEmployer(Employer employer);
}
