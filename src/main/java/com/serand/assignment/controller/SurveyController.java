package com.serand.assignment.controller;


import com.serand.assignment.model.SubmittedSurvey;
import com.serand.assignment.model.Survey;
import com.serand.assignment.repository.SurveyRepository;
import com.serand.assignment.repository.SurveySubmissionRepository;
import com.serand.assignment.services.SurveyService;
import com.serand.assignment.vo.requestVo.CreateSurveyVo;
import com.serand.assignment.vo.responseVo.SurveyResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Responsible for all user related endpoints.
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/surveys")
public class SurveyController {

	@Autowired
	private SurveyRepository surveyRepository;
    @Autowired
    private SurveySubmissionRepository submissionRepository;
    @Autowired
    private SurveyService surveyService;

    @PostMapping("/{jobId}")
    public ResponseEntity<Survey> createSurvey(@PathVariable String jobId, @RequestBody CreateSurveyVo survey){
        Survey createSurvey= surveyService.createSurvey(jobId, survey);
        return ResponseEntity.status(HttpStatus.CREATED).body(createSurvey);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Survey> deleteSurvey(@PathVariable("id") String surveyId){
        if (!surveyRepository.existsById(surveyId)) {
            throw new RuntimeException("Survey not found");
        }
        surveyRepository.deleteById(surveyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/submit")
    public ResponseEntity<SubmittedSurvey> submitSurvey(@RequestBody SubmittedSurvey surveySubmission){
       SubmittedSurvey submittedSurvey = surveyService.submitSurvey(surveySubmission);
        return ResponseEntity.status(HttpStatus.CREATED).body(submittedSurvey);
    }

    @GetMapping("/search")
    public ResponseEntity<List<SurveyResultVo>> searchCompletedSurveys(@RequestParam(required = false) Integer score, @RequestParam(required = false) String job) {
        return ResponseEntity.ok().body(surveyService.searchCompletedSurveys(score, job));
    }

}