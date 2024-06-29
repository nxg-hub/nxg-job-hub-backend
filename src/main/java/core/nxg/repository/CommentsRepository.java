package core.nxg.repository;

import core.nxg.entity.Comments;
import core.nxg.entity.JobPosting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CommentsRepository extends MongoRepository<Comments, String> {
    List<Comments> findByJobPosting(JobPosting jobPosting);



}
