package core.nxg.repository;

import core.nxg.entity.SavedJobs;
import core.nxg.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedJobRepository extends JpaRepository<SavedJobs, Long> {
        Page<SavedJobs> findByUser(User user, Pageable pageable);
    }

