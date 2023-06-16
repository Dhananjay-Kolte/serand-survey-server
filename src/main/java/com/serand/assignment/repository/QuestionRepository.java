package com.serand.assignment.repository;

import com.serand.assignment.model.Question;
import com.serand.assignment.model.SubmittedSurvey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
}
