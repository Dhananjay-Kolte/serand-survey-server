package com.serand.assignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "surveySubmission")
public class SubmittedSurvey {
    @Id
    private String id;
    private String surveyId;
    private String candidateId;
    private String jobId;
    private Integer score;
    private String status;
    private Map<String, List<String>> answers;

}
