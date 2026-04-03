package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.dto.AnswerDto;
import com.naukri.central_api.dto.FormSubmissionDto;
import com.naukri.central_api.dto.JobSearchRequestDto;
import com.naukri.central_api.exception.UnAuthorizedException;
import com.naukri.central_api.model.*;
import com.naukri.central_api.utility.MappingUtility;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    DbApiConnector dbApiConnector;
    UserService userService;
    AnswerService answerService;
    MappingUtility mappingUtility;
    FormSubmissionService formSubmissionService;

    public JobService(DbApiConnector dbApiConnector, UserService userService,
                      AnswerService answerService, MappingUtility mappingUtility,
                      FormSubmissionService formSubmissionService){
        this.dbApiConnector = dbApiConnector;
        this.userService = userService;
        this.answerService = answerService;
        this.mappingUtility = mappingUtility;
        this.formSubmissionService = formSubmissionService;
    }

//    public List<Job> searchJobs(String title, String companyName, String location){
//        //create SQL auery
//    }
//
//    public String createJobSearch(String title, String companyName, String location){
//
//    }

    public Job saveJob(Job job){
        return dbApiConnector.callSaveJobEndpoint(job);
    }

    public ApplicationForm getApplicationFormByJobId(UUID jobId, String Authorization){

        String token = Authorization.substring(7);
        AppUser user = userService.getUserFromToken(token);

        if(!user.getUserType().equals("JOB_SEEKER")){
            throw new UnAuthorizedException("Operation Not Allowed");
        }
        // do we have any functionality to get job by jobId
        return this.getJobById(jobId).getApplicationForm();
    }

    public Job getJobById(UUID jobId){
        //do we have any function in dbApiConnector which is calling get job by id end point
        return dbApiConnector.callGetJobById(jobId);
    }

    public FormSubmission submitForm(FormSubmissionDto formSubmissionDto,
                                     UUID jobId,
                                     String Authorization){
        String token = Authorization.substring(7);
        AppUser jobSeeker = userService.getUserFromToken(token);
        if(!jobSeeker.getUserType().equals("JOB_SEEKER")){
            throw new UnAuthorizedException("Not Allowed");
        }
        List<Answer> answers = answerService.getAlAnswers(formSubmissionDto.getAnswers());
        //we need to map details to formSubmission model
        FormSubmission formSubmission = mappingUtility.createFormSubmissionFromDto(answers, jobSeeker);
        //we need to save the form submission object by calling db
        formSubmission =  formSubmissionService.saveForm(formSubmission);
        Job job = this.getJobById(jobId);
        job.getJobApplications().add(formSubmission);
        this.saveJob(job);

        return formSubmission;
    }

    public List<AppUser> getAppUserBySkill(Skill skillName){
        return userService.getAppuserBySkillName(skillName);
    }

    public List<Job> searchJob(JobSearchRequestDto request){
        return dbApiConnector.searchJob(request);
    }

    public List<Job> appliedJobs(AppUser user){

    }
}
