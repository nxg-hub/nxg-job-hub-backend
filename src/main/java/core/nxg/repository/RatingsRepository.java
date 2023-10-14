package core.nxg.repository;

import core.nxg.entity.Employer;
import core.nxg.entity.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingsRepository extends JpaRepository<Ratings, Long> {
    List<Ratings> findAllByEmployer(Employer employer);
}
