package com.naukri.central_api.utility;

import com.naukri.central_api.dto.*;
import com.naukri.central_api.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MappingUtility {


    public AppUser mapJobSeekerDetailsToAppUser(JobSeekerRegistrationDto jobSeekerDto, List<Skill> skills){
        AppUser appUser = new AppUser();
        appUser.setUserType("JOB_SEEKER");
        appUser.setName(jobSeekerDto.getName());
        appUser.setEmail(jobSeekerDto.getEmail());
        appUser.setPassword(jobSeekerDto.getPassword());
        appUser.setPhoneNumber(jobSeekerDto.getPhoneNumber());
        //to get List<Skill> from  List<String>
        appUser.setSkillSet(skills);

        return appUser;
    }

    public Company mapCompanyDetailsToCompany(CompanyRegistrationDto companyRegistrationDto){

        Company company = new Company();

        company.setCompanyName(companyRegistrationDto.getCompanyName());
        company.setEmail(companyRegistrationDto.getEmail());
        company.setLinkedinLink(companyRegistrationDto.getLinkedinLink());
        company.setWebsiteLink(companyRegistrationDto.getWebsiteLink());
        company.setCompanySize(companyRegistrationDto.getCompanySize());
        company.setIndustry(companyRegistrationDto.getIndustry());

        return company;
    }

    public AppUser mapCompanyDtoToAdmin(CompanyRegistrationDto companyRegistrationDto ,Company company){
        AppUser admin = new AppUser();
        admin.setCompany(company);
        admin.setName("Admin");
        admin.setPassword(companyRegistrationDto.getPassword());
        admin.setEmail(companyRegistrationDto.getEmail());
        admin.setPhoneNumber(companyRegistrationDto.getPhoneNumber());
        admin.setUserType("ADMIN");

        return admin;
    }

    public AppUser mapRecruiterDtoToAppUser(RecruiterDetailsDto recruiterDetailsDto, Company company){

        AppUser user = new AppUser();
        user.setName(recruiterDetailsDto.getName());
        user.setEmail(recruiterDetailsDto.getEmail());
        user.setPhoneNumber(recruiterDetailsDto.getPhoneNumber());
        user.setUserType("RECRUITER");
        user.setPassword("DefaultPass123");
        user.setCompany(company);
        user.setStatus("INACTIVE");

        return user;
    }

    public Questions createQuestionFromQuestionName(String questionName, boolean isMandatory){
        Questions q = new Questions();
        q.setQuestion(questionName);
        q.setMandatory(isMandatory);
        return q;
    }

    public Job createJobFromJobDto(CreateJobDto createJobDto,ApplicationForm applicationForm, List<Skill> skill,
                                   AppUser recruiter){
        Job job = new Job();
        job.setCreatedBy(recruiter);
        job.setJobDescription(createJobDto.getJobDescription());
        job.setJobApplications(new ArrayList<>());
        job.setLocation(createJobDto.getLocation());
        job.setSkills(skill);
        job.setApplicationForm(applicationForm);
        job.setState("DRAFT");
        job.setShortDescription(createJobDto.getShortDescription());

        if(createJobDto.getState().equals("POSTED")){
            job.setPostedDate(LocalDateTime.now());
        }
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        return job;
    }

    public FormSubmission createFormSubmissionFromDto(List<Answer> answers, AppUser jobSeeker){
        FormSubmission form = new FormSubmission();
        form.setAnswers(answers);
        form.setJobSeeker(jobSeeker);

        return form;
    }
}
