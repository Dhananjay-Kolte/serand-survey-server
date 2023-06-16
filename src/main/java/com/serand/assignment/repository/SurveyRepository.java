package com.serand.assignment.repository;

import com.serand.assignment.model.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, String> {

//    List<Survey> findByCompanyId(String companyId);
}