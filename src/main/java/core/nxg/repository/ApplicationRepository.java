package core.nxg.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import core.nxg.entity.Application;
import core.nxg.entity.JobPosting;

import java.util.List;
import java.util.Optional;

import core.nxg.entity.TechTalentUser;
import core.nxg.entity.User;
import core.nxg.enums.ApplicationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


public interface ApplicationRepository extends MongoRepository<Application, String> {
    Page<Application> findByApplicant(User applicant, Pageable pageable) throws Exception;
    Optional<List<Application> >findByJobPosting(JobPosting jobPosting, Pageable pageable) throws Exception;
    Optional<List<Application>>findByApplicationStatus(ApplicationStatus applicationStatus, Pageable pageable);
    Application findByApplicationId(Long applicationId);
    // In your ApplicationRepository interface
//    Page<Application> findByJob(JobPosting jobPosting, Pageable pageable);

}
