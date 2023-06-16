package com.serand.assignment.vo.requestVo;

import com.serand.assignment.model.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSurveyVo {

    private String name;
    private List<Question> surveyquestions;
}
