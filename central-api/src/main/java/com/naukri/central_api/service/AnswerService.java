package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.dto.AnswerDto;
import com.naukri.central_api.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {

    QuestionService questionService;
    DbApiConnector dbApiConnector;

    @Autowired
    public AnswerService(QuestionService questionService, DbApiConnector dbApiConnector){
        this.questionService = questionService;
        this.dbApiConnector = dbApiConnector;
    }

    public List<Answer> getAlAnswers(List<AnswerDto> answerDtos){
        List<Answer> answers = new ArrayList<>();
        for(AnswerDto answerDto : answerDtos){
            Answer answer = new Answer();
            UUID questionId = answerDto.getQuestionId();
            answer.setAnswer(answerDto.getAnswer());
            answer.setQuestion(questionService.getQuestionById(questionId));
            answers.add(this.saveAnswer(answer));
        }
            return  answers;
    }

    public Answer saveAnswer(Answer answer){
        return dbApiConnector.callSaveAnswerEndpoint(answer);
    }
}
