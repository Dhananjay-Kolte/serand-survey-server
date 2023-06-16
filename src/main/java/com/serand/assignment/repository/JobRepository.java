package com.serand.assignment.repository;

import com.serand.assignment.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface JobRepository extends MongoRepository<Job, String> {
    }
