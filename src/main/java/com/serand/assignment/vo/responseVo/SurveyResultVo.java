package com.serand.assignment.vo.responseVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultVo {

    private String surveyQuestion;
    private List<String> surveyAnswer;
    private String surveyTitle;
    private int surveyScore;
}
