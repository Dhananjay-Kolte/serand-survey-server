package com.serand.assignment.services;

import com.serand.assignment.mapper.SurveyResultMapper;
import com.serand.assignment.model.*;
import com.serand.assignment.repository.*;
import com.serand.assignment.vo.requestVo.CreateSurveyVo;
import com.serand.assignment.vo.responseVo.SurveyResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveySubmissionRepository submissionRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SurveyResultMapper surveyResultMapper;
    @Autowired
    private CandidateRepository candidateRepository;

    public Survey createSurvey(String jobId, CreateSurveyVo survey) {
        try {
            List<String> questionIds = survey.getSurveyquestions().stream().map(question -> questionRepository.save(question).getId()).collect(Collectors.toList());
            Survey newSurvey = new Survey();
            newSurvey.setName(survey.getName());
            newSurvey.setQuestionIds(questionIds);
            Survey savedSurvey = surveyRepository.save(newSurvey);
            Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found..."));
            job.setSurveyId(savedSurvey.getId());
            jobRepository.save(job);
            return savedSurvey;
        } catch (Exception e) {
            throw new RuntimeException("Error creating survey", e);
        }
    }

    public List<SurveyResultVo> searchCompletedSurveys(Integer score, String job) {
        List<SubmittedSurvey> completedSurveys = getCompletedSurveys(score, job);
        return surveyResultMapper.mapToSurveyResultVo(completedSurveys);
    }

    private List<SubmittedSurvey> getCompletedSurveys(Integer score, String job) {
        if (score != null && job != null) {
            return submissionRepository.findByScoreAndJobId(score, job);
        } else if (score != null) {
            return submissionRepository.findByScore(score);
        } else if (job != null) {
            return submissionRepository.findByJobId(job);
        } else {
            return submissionRepository.findAll();
        }
    }

    public SubmittedSurvey submitSurvey(SubmittedSurvey surveySubmission) {
        submissionRepository.save(surveySubmission);
        String surveyId = surveySubmission.getSurveyId();
        String jobId = surveySubmission.getJobId();
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Survey not found..."));
        List<String> questionIds = survey.getQuestionIds();
        Map<String, List<String>> answers = surveySubmission.getAnswers();

        int totalScore = 0;
        Map<String, Question> questionMap = questionRepository.findAllById(questionIds)
                .stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));
        for (String questionId : questionIds) {
            Question question = questionMap.get(questionId);
            List<String> questionAnswers = answers.get(questionId);

            if (question != null && questionAnswers != null) {
                int score = calculateScore(question, questionAnswers);
                totalScore += score;
            }
        }
        surveySubmission.setScore(totalScore);
        surveySubmission.setStatus("COMPLETED");
        Candidate candidate = candidateRepository.findById(surveySubmission.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found..."));
        candidate.getAppliedJobs().add(jobId);
        candidateRepository.save(candidate);
        return submissionRepository.save(surveySubmission);
    }


    public int calculateScore(Question question, List<String> answers) {
        int score = 0;
        switch (question.getType()) {
            case SINGLE:
                score = calculateScoreForSingle(question, answers);
                break;
            case MULTIPLE:
                score = calculateScoreForMultiple(question, answers);
                break;
            case BONUS:
                score = calculateScoreForBonus(question, answers);
                break;
            default:
                score = calculateScoreForSingle(question, answers);
                break;
        }
        return score;
    }

    private int calculateScoreForSingle(Question question, List<String> answers) {
        String expectedAnswer = question.getExpectedAnswers().stream()
                .findFirst()
                .orElse(null);
        String userAnswer = answers.get(0);
        return userAnswer.equalsIgnoreCase(expectedAnswer) ? question.getPoints() : 0;
    }

    private int calculateScoreForMultiple(Question question, List<String> answers) {
        int score = 0;
        List<String> expectedAnswers = question.getExpectedAnswers();
        for (String answer : answers) {
            if (expectedAnswers.contains(answer)) {
                score += question.getPoints();
            }
        }
        return score;
    }

    private int calculateScoreForBonus(Question question, List<String> answers) {
        int score = 0;
        List<String> expectedAnswers = question.getExpectedAnswers();
        int bonusPoint = question.getBonusPoint();
        int regularPoint = question.getPoints();
        for (String answer : answers) {
            if (expectedAnswers.contains(answer)) {
                score += bonusPoint;
            } else {
                score += regularPoint;
            }
        }
        return score;
    }


}
