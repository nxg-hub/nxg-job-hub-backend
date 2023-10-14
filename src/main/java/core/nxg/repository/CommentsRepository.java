package core.nxg.repository;

import core.nxg.entity.Comments;
import core.nxg.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findByJobPosting(JobPosting jobPosting);



}
