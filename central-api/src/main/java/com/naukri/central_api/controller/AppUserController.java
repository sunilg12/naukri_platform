package com.naukri.central_api.controller;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.dto.FormSubmissionDto;
import com.naukri.central_api.dto.JobSeekerRegistrationDto;
import com.naukri.central_api.dto.JwtTokenResponseDto;
import com.naukri.central_api.dto.LoginDto;
import com.naukri.central_api.exception.UnAuthorizedException;
import com.naukri.central_api.model.*;
import com.naukri.central_api.service.FormSubmissionService;
import com.naukri.central_api.service.JobService;
import com.naukri.central_api.service.UserService;
import com.naukri.central_api.utility.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/central/user")
public class AppUserController {

        UserService userService;
        AuthUtil authUtil;
        JobService jobService;
        FormSubmissionService formSubmissionService;

        public AppUserController(UserService userService,  AuthUtil authUtil,
                                 JobService jobService, FormSubmissionService formSubmissionService){
            this.userService = userService;
            this.authUtil = authUtil;
            this.jobService = jobService;
            this.formSubmissionService = formSubmissionService;
        }

    @PostMapping("/register")
    public ResponseEntity registerJobApplicant(@RequestBody JobSeekerRegistrationDto jobSeekerDto){
            //calling userService
        AppUser user =  userService.registerJobSeeker(jobSeekerDto);
        String token = authUtil.generateToken(user.getEmail(),user.getPassword(),user.getUserType());
        JwtTokenResponseDto tokenResponseDto = new JwtTokenResponseDto(token);

        return new ResponseEntity(tokenResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity userLogin(@RequestBody LoginDto loginDto){
        try{
            String token = authUtil.generateTokenFromLoginDetails(loginDto.getEmail(),loginDto.getPassword());
            return new ResponseEntity(new JwtTokenResponseDto(token), HttpStatus.OK);
        }catch(UnAuthorizedException e){
            return new ResponseEntity(e, HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/job/search")
//    public ResponseEntity searchJob(
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String company,
//            @RequestParam(required = false) String location
//    ){
//            //call jobService
//
//    }

    @GetMapping("/{jobId}/application-form")
    public ApplicationForm getApplicationFormByJobId(@PathVariable UUID jobId,@RequestHeader String Authorization){

            //from here we call jobService
        return jobService.getApplicationFormByJobId(jobId, Authorization);
    }

    @PostMapping("/for/submit/{jobId}")
    public FormSubmission submitForm(@RequestBody FormSubmissionDto form,
                                     @RequestHeader String Authorization,
                                     @PathVariable UUID jobId){
        // we call submitForm method

        return jobService.submitForm(form, jobId, Authorization);
    }

    @GetMapping("/skill/getUserBySkillName")
    public List<AppUser> getAppUserBySkillName(@RequestBody Skill skillName){
            return userService.getAppuserBySkillName(skillName);
    }

}
