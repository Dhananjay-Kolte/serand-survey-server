package com.serand.assignment.repository;

import com.serand.assignment.model.Candidate;
import com.serand.assignment.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {

}