package core.nxg.algorithm;

import core.nxg.entity.User;
import org.springframework.stereotype.Service;


public interface JobAlgorithm {


    Object getJobs(User user);

    Object removeDuplicate();











}
