package com.serand.assignment.repository;

import com.serand.assignment.model.SubmittedSurvey;
import com.serand.assignment.model.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SurveySubmissionRepository extends MongoRepository<SubmittedSurvey, String> {

    List<SubmittedSurvey> findByScore(Integer score);
    List<SubmittedSurvey> findByJobId(String jobId);
    List<SubmittedSurvey> findByScoreAndJobId(int score, String jobId);
}
