package com.naukri.central_api.controller;

import com.naukri.central_api.dto.CompanyRegistrationDto;
import com.naukri.central_api.dto.CreateJobDto;
import com.naukri.central_api.dto.JwtTokenResponseDto;
import com.naukri.central_api.dto.RecruiterDetailsDto;
import com.naukri.central_api.exception.UnAuthorizedException;
import com.naukri.central_api.model.AppUser;
import com.naukri.central_api.model.Company;
import com.naukri.central_api.model.Job;
import com.naukri.central_api.service.CompanyService;
import com.naukri.central_api.utility.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/central/company")
public class CompanyController {

    CompanyService companyService;
    AuthUtil authUtil;

    @Autowired
    public CompanyController(CompanyService companyService,AuthUtil authUtil){
        this.companyService = companyService;
        this.authUtil = authUtil;
    }

    @PostMapping("/register")
    public ResponseEntity registerCompany(@RequestBody CompanyRegistrationDto companyRegistrationDto){

        // companyservice to save company details in database
        Company company = companyService.registerCompany(companyRegistrationDto);
        String token = authUtil.generateToken(companyRegistrationDto.getEmail(), companyRegistrationDto.getPassword(),
                        "ADMIN");
        JwtTokenResponseDto tokenResponseDto = new JwtTokenResponseDto(token);
        return new ResponseEntity(tokenResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/invite-recruiter")
    public ResponseEntity inviteRecruiter(@RequestBody RecruiterDetailsDto recruiterDetailsDto,
                                          @RequestHeader String Authorization){
        AppUser recruiter = companyService.inviteRecruiter(recruiterDetailsDto, Authorization);

        return new ResponseEntity(recruiter,HttpStatus.CREATED);
    }

    @GetMapping("/accept-invitation/{token}")
    public ResponseEntity acceptInvitation(@PathVariable String token){
        try{
            AppUser recruiter = companyService.acceptInvitation(token);
            return new ResponseEntity(recruiter, HttpStatus.CREATED);
        }
        catch (UnAuthorizedException e){
            return new ResponseEntity(e, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/job/create")
    public ResponseEntity createJob(@RequestBody CreateJobDto createJobDto, @RequestHeader String Authorization){
        try{
            Job job = companyService.createJob(createJobDto,Authorization);
            return new ResponseEntity(job, HttpStatus.CREATED);
        }
        catch(UnAuthorizedException e){
            return new ResponseEntity(e, HttpStatus.UNAUTHORIZED);
        }
    }
}
