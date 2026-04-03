package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.connectors.NotificationApiConnector;
import com.naukri.central_api.dto.CompanyRegistrationDto;
import com.naukri.central_api.dto.CreateJobDto;
import com.naukri.central_api.dto.RecruiterDetailsDto;
import com.naukri.central_api.exception.UnAuthorizedException;
import com.naukri.central_api.model.*;
import com.naukri.central_api.utility.AuthUtil;
import com.naukri.central_api.utility.MappingUtility;
import org.apache.catalina.webresources.ClasspathURLStreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    MappingUtility mappingUtility;
    DbApiConnector dbApiConnector;
    UserService userService;
    NotificationApiConnector notificationApiConnector;
    AuthUtil authUtil;
    ApplicationFormService applicationFormService;
    SkillService skillService;
    JobService jobService;

    @Autowired
    public CompanyService(MappingUtility mappingUtility,DbApiConnector dbApiConnector, UserService userService,
                          NotificationApiConnector notificationApiConnector, AuthUtil authUtil,
                          ApplicationFormService applicationFormService,SkillService skillService,
                          JobService jobService){
        this.mappingUtility = mappingUtility;
        this.dbApiConnector = dbApiConnector;
        this.userService = userService;
        this.notificationApiConnector = notificationApiConnector;
        this.authUtil = authUtil;
        this.applicationFormService = applicationFormService;
        this.skillService = skillService;
        this.jobService = jobService;
    }
    /**
     * Expectations of this function is to save Company details in the Company table
     * To save the Company details it calls the Database Api Connector which hits the  request
     * to database Api Company Controller
     * @return company
     * */

    public Company registerCompany(CompanyRegistrationDto companyRegistrationDto){

        // we need to map the companyRegistrationDto to Company class
        Company company = mappingUtility.mapCompanyDetailsToCompany((companyRegistrationDto));
        company = this.saveCompany(company);
        // we should create admin account for company
        AppUser admin = mappingUtility.mapCompanyDtoToAdmin(companyRegistrationDto,company);
        // we should save admin in the database by using userService
        userService.saveAppUser(admin);

        return company;
    }

    /**
     * this internally calls database api connector which will call database Api which will be calling
     * save company endpoint
     * @return company*/
    public Company saveCompany(Company company){

        // we will call database Api connector to save Company in the company
        return dbApiConnector.callSaveCompanyEndpoint(company);
    }

    public AppUser inviteRecruiter(RecruiterDetailsDto recruiterDetailsDto, String Authorization){

        String token = Authorization.substring(7);
        AppUser admin = userService.getUserFromToken(token);

        if(!userService.isAdminUser(admin)){
            throw new UnAuthorizedException("Invalid Exception");
        }
        Company company = admin.getCompany();
        // we need to create user object for the recruiter
        AppUser recruiter = mappingUtility.mapRecruiterDtoToAppUser(recruiterDetailsDto,company);
        recruiter = userService.saveAppUser(recruiter);
        token = authUtil.generateToken(recruiter.getEmail(),recruiter.getPassword(), "RECRUITER");
        //mail recruiter
        //we need to write logic such that we will be able to notify the recruiter that they are invited to this company
        //so to send the mail to the user we need to call the notification api invite recruiter endpoint such that the
        // recruiter will receive mail
        notificationApiConnector.callInviteRecruiterEndpoint(recruiter, token);

        return recruiter;
    }

    public AppUser acceptInvitation(String token){
        String[] payload = userService.decryptJwtToken(token).split(":");
        String email = payload[0];
        String password = payload[1];
        String role = payload[2];

        if(!userService.validateCredentials(email, password)){
            throw new UnAuthorizedException("Invalid credentials");
        }

        AppUser recruiter = userService.getUserFromToken(token);
        if(!userService.isRecruiter(recruiter)){
            throw new UnAuthorizedException("Invalid Operation");
        }
        recruiter.setStatus("ACTIVE");
        userService.saveAppUser(recruiter);
        // mail to company admin that this user has accepted the invitation
        String adminEmail = recruiter.getCompany().getEmail();
        AppUser admin = userService.dbApiConnector.callGetUserByEmailEndpoint(adminEmail);
        List<AppUser> mailDetails = new ArrayList<>();
        mailDetails.add(recruiter);
        mailDetails.add(admin);
        //so we need to call notificationApi
        notificationApiConnector.callAcceptInvitationEndpoint(mailDetails);

        return recruiter;
    }

    public Job createJob(CreateJobDto createJobDto, String Authorization){
        String token = authUtil.extractTokenFromBearerToken(Authorization);
        AppUser recruiter = userService.getUserFromToken(token);
        if(!userService.isRecruiter(recruiter)){
            throw new UnAuthorizedException("Not Authorized");
        }
        //need to map createJob dto model
        //we need to think about what are necessary details
        // as it is depended on applicationForm object so we need to create Applicationform
        ApplicationForm applicationForm = applicationFormService.createApplicationFormByQuestions(createJobDto.getQuestions());
        List<Skill> skill = skillService.getAllSkills(createJobDto.getSkill());

        Job job = mappingUtility.createJobFromJobDto(createJobDto, applicationForm,skill, recruiter);

        return jobService.saveJob(job);
    }

    public List<AppUser> getAppUserBySkillName(Skill name){
        return jobService.getAppUserBySkill(name);
    }
}
