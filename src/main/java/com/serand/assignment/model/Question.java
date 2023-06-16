package com.serand.assignment.model;

import com.serand.assignment.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "question")
public class Question {
    @Id
    private String id;
    private String questionTitle;
    private QuestionType type;
    private List<String> options;
    private int points;
    private  int bonusPoint;
    private List<String> expectedAnswers;
}
