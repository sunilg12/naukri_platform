package com.naukri.central_api.connectors;

import com.naukri.central_api.dto.JobSearchRequestDto;
import com.naukri.central_api.model.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class DbApiConnector extends RestApi{
    // we need to call database Api endpoints from this class to write calling methods

    @Value("${database.api.baseurl}")
    String baseUrl;

    ModelMapper modelMapper = new ModelMapper();

    /**
     * this function make requests to database Api to get user email endpoint
     * @param email
     * @return AppUser*/

    public AppUser callGetUserByEmailEndpoint(String email){
        String url = baseUrl + "/user/email/" + email;
        Object resp = this.makeGetCall(url, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, AppUser.class);
    }


    public Skill callGetSkillByNameEndpoint(String skillName){
        // in this method we will call getBySkillName endpoint from dbApi;
        // create url
        String url = baseUrl + "/skill/get/name/" + skillName;
        //creation of request
        Object resp = this.makeGetCall(url, new HashMap<>());
        if(resp == null){
            return null;
        }

        return  modelMapper.map(resp, Skill.class);
    }

    public AppUser callSaveUserEndpoint(AppUser user){

        String url = baseUrl + "/user/save";
        Object resp = this.makePostCall(url, user, new HashMap<>());
        if(resp == null){
            return null;
        }
        return modelMapper.map(resp, AppUser.class);
    }

    public Skill callSaveSkillEndpoint(Skill skill){

        String url = baseUrl + "/skill/save";
        Object resp = this.makePostCall(url, skill, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Skill.class);
    }

    public Company callSaveCompanyEndpoint(Company company){
        String url = baseUrl + "/company/save";
        Object resp = this.makePostCall(url, company, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Company.class);
    }

    public Questions callCreateQuestionsEndpoint(Questions questions){
        String url = baseUrl + "/question/save";
        Object resp = this.makePostCall(url, questions, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Questions.class);
    }

    public Job callCreateQuestionsEndpoint(Job job){
        String url = baseUrl + "/job/save";
        Object resp = this.makePostCall(url, job, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Job.class);
    }

    public Job callSaveJobEndpoint(Job job){
        String url = baseUrl + "/job/save";
        Object resp = this.makePostCall(url, job, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Job.class);
    }

    public ApplicationForm callSaveApplicationFormEndpoint(ApplicationForm applicationForm){
        String url = baseUrl + "/form/save";
        Object resp = this.makePostCall(url, applicationForm, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, ApplicationForm.class);
    }

    public Job callGetJobById(UUID jobId){
        String url = baseUrl + "/job/" + jobId.toString();
        Object resp = this.makeGetCall(url, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Job.class);
    }

    public Questions callGetQuestionById(UUID questionId){
        String url = baseUrl + "/question/" + questionId.toString();
        Object resp = this.makeGetCall(url, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Questions.class);
    }

    public Answer callSaveAnswerEndpoint(Answer answer){
        String url = baseUrl + "/answer/save";
        Object resp = this.makePostCall(url, answer, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, Answer.class);
    }

    public FormSubmission callSaveFormSubmissionEndpoint(FormSubmission formSubmission){
        String url = baseUrl + "/formSubmission/save";
        Object resp = this.makePostCall(url, formSubmission, new HashMap<>());
        if(resp == null){
            return null;
        }

        return modelMapper.map(resp, FormSubmission.class);
    }

    public List<Job> searchJob(JobSearchRequestDto request){
        String url = baseUrl + "/job/searchJob";
        Object resp = this.makePostCall(url, request, new HashMap<>());
        if(resp == null){
            return null;
        }

        return  (List<Job>) resp;
    }

//    public List<AppUser> callGetAppUserBySkillName(Skill skillName){
//        String url = baseUrl + "/getSkillBySkillName";
//         re
//    }
}
