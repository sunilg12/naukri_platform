package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.model.ApplicationForm;
import com.naukri.central_api.model.Questions;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationFormService {

    QuestionService questionService;
    DbApiConnector dbApiConnector;

    public ApplicationFormService(QuestionService questionService, DbApiConnector dbApiConnector){
        this.questionService = questionService;
        this.dbApiConnector = dbApiConnector;
    }

    public ApplicationForm createApplicationFormByQuestions(List<String> questionList){
        // we are getting list of questions in string
        // we need to create applicationform we require list of questions
        List<Questions>  questions = questionService.getAllQuestions(questionList);
        ApplicationForm applicationForm = new ApplicationForm();
        applicationForm.setQuestionsList(questions);
        return this.saveApplicationForm(applicationForm);
    }

    public ApplicationForm saveApplicationForm(ApplicationForm applicationForm){
        //call the dbapi Connector;
        return dbApiConnector.callSaveApplicationFormEndpoint(applicationForm);
    }

}
