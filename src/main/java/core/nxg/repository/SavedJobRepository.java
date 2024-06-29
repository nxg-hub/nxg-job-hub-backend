package core.nxg.repository;

import core.nxg.entity.JobPosting;
import core.nxg.entity.SavedJobs;
import core.nxg.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SavedJobRepository extends MongoRepository<SavedJobs, String> {
        Page<SavedJobs> findByUser(User user, Pageable pageable);

    Optional<SavedJobs> findByUserAndJobPosting(User user, JobPosting jobPosting);
}

