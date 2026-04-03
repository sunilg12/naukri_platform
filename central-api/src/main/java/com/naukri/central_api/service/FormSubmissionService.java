package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.model.FormSubmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormSubmissionService {

    DbApiConnector dbApiConnector;

    @Autowired
    public FormSubmissionService(DbApiConnector dbApiConnector){
        this.dbApiConnector = dbApiConnector;
    }

    public FormSubmission saveForm(FormSubmission formSubmission){
        return dbApiConnector.callSaveFormSubmissionEndpoint(formSubmission);
    }
}
