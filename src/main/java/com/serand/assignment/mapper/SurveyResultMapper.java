package com.serand.assignment.mapper;

import com.serand.assignment.model.Question;
import com.serand.assignment.model.SubmittedSurvey;
import com.serand.assignment.model.Survey;
import com.serand.assignment.repository.QuestionRepository;
import com.serand.assignment.repository.SurveyRepository;
import com.serand.assignment.vo.responseVo.SurveyResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SurveyResultMapper {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;


    public List<SurveyResultVo> mapToSurveyResultVo(List<SubmittedSurvey> completedSurveys) {
        // Fetch all question IDs from the completed surveys
        Set<String> questionIds = completedSurveys.stream()
                .flatMap(survey -> survey.getAnswers().keySet().stream())
                .collect(Collectors.toSet());

        // Fetch all questions from the repository based on the question IDs
        List<Question> questions = questionRepository.findAllById(questionIds);

        // Create a map of question IDs to questions for easy lookup
        Map<String, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getId, Function.identity()));

        // Fetch all survey IDs from the completed surveys
        Set<String> surveyIds = completedSurveys.stream()
                .map(SubmittedSurvey::getSurveyId)
                .collect(Collectors.toSet());

        // Fetch all surveys from the repository based on the survey IDs
        List<Survey> surveys = surveyRepository.findAllById(surveyIds);

        // Create a map of survey IDs to survey names for easy lookup
        Map<String, String> surveyNameMap = surveys.stream()
                .collect(Collectors.toMap(Survey::getId, Survey::getName));

        List<SurveyResultVo> surveyResults = new ArrayList<>();

        for (SubmittedSurvey submittedSurvey : completedSurveys) {
            Map<String, List<String>> answers = submittedSurvey.getAnswers();

            for (Map.Entry<String, List<String>> entry : answers.entrySet()) {
                String questionId = entry.getKey();
                List<String> userAnswers = entry.getValue();

                Question question = questionMap.get(questionId);

                if (question != null) {
                    SurveyResultVo surveyResult = new SurveyResultVo();
                    surveyResult.setSurveyQuestion(question.getQuestionTitle());
                    surveyResult.setSurveyAnswer(userAnswers);
                    surveyResult.setSurveyTitle(surveyNameMap.get(submittedSurvey.getSurveyId()));
                    surveyResult.setSurveyScore(submittedSurvey.getScore());
                    surveyResults.add(surveyResult);
                }
            }
        }

        return surveyResults;
    }

}
