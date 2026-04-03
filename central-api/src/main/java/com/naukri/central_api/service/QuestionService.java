package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.model.Questions;
import com.naukri.central_api.utility.MappingUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    DbApiConnector dbApiConnector;
    MappingUtility mappingUtility;

    @Autowired
    public QuestionService(DbApiConnector dbApiConnector, MappingUtility mappingUtility){
        this.dbApiConnector = dbApiConnector;
        this.mappingUtility = mappingUtility;
    }

    public List<Questions> getAllQuestions(List<String> questionList){
        List<Questions> questions = new ArrayList<>();
        for(String question : questionList){
             Questions q = mappingUtility.createQuestionFromQuestionName(question,true);
             q = this.saveQuestion(q);
             questions.add(q);
        }
        return questions;
    }

    public Questions getQuestionById(UUID questionId){
        return dbApiConnector.callGetQuestionById(questionId);
    }


    /**
     * it saves Questions inside the DbApiConnector
     * @param questions
     * @return
     * */

    public Questions saveQuestion(Questions questions){
        return dbApiConnector.callCreateQuestionsEndpoint(questions);
    }
}
