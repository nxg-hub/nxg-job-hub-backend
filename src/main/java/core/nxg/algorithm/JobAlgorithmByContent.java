package core.nxg.algorithm;

import core.nxg.entity.JobPosting;
import core.nxg.entity.User;
import core.nxg.repository.JobPostingRepository;
import core.nxg.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
public class JobAlgorithmByContent implements JobAlgorithm{

    private JobPostingRepository jobRepository;


    private UserRepository userRepository;

    /*
    * [1] The trick is to check for the skills of the techtalent to be recommended jobs;
    *
    * And compare with "tags" in each job posting entity existing.
    *
    * And return.
    *
    * [2] But be sure a job is not recommended twice by hash mapping the job id as the key
    * to the job posting object (as value);
    *
    *
    */

    @Override
    public Object getJobs(User user) {

        Collection<?> recommendedJobs = null;

        var jobs = jobRepository.findAll();

        var userSkills = user.getTechTalent().getSkills();


         recommendedJobs = jobs.stream()
                .filter(job -> CollectionUtils.containsAny(job.getTags(), userSkills))
                .toList();


        if (!CollectionUtils.isEmpty(recommendedJobs)) {
            return Collections.EMPTY_LIST;
        }


        return recommendedJobs;
    }

    @Override
    public Object removeDuplicate() {
        return null;
    }
}
