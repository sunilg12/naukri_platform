package com.naukri.database_api.controllers;

import main.com.naukri.database_api.enums.ApplicationStatus;
import main.com.naukri.database_api.models.AppUser;
import main.com.naukri.database_api.models.Job;
import com.naukri.database_api.models.JobApplication;
import com.naukri.database_api.repository.AppUserRepository;
import main.com.naukri.database_api.repository.JobApplicationRepository;
import main.com.naukri.database_api.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/db/jobApplication")
public class JobApplicationController {

    JobRepository jobRepository;
    AppUserRepository appUserRepository;
    JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationController(JobRepository jobRepository,AppUserRepository appUserRepository,
                                    JobApplicationRepository jobApplicationRepository){
        this.jobRepository = jobRepository;
        this.appUserRepository = appUserRepository;
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @PostMapping("/apply")
    public ResponseEntity saveJob(@RequestParam UUID jobId, @RequestParam UUID userId){
        Job job = jobRepository.findById(jobId).orElse(null);
        AppUser user = appUserRepository.findById(userId).orElse(null);

        if(job == null || user == null){
            return ResponseEntity.badRequest().body("Invalid details");
        }
        JobApplication application = new JobApplication();
        application.setApplicant(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);

        jobApplicationRepository.save(application);

        return new ResponseEntity(application, HttpStatus.CREATED);
    }

    @GetMapping("/job/{JobId}")
    public ResponseEntity getByJobId(@PathVariable UUID jobId){
        return  ResponseEntity.ok(jobApplicationRepository.findByJobId(jobId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getByUser(@PathVariable UUID userId){
        return ResponseEntity.ok(jobApplicationRepository.findByApplicantId(userId));
    }

    @PutMapping("/id/{status}")
    public ResponseEntity updateStatus(@PathVariable UUID id, @RequestParam String status){
        JobApplication app = jobApplicationRepository.findById(id).orElse(null);
        if(app == null){
            return ResponseEntity.badRequest().body("Incorrect Details");
        }

        app.setStatus(ApplicationStatus.valueOf(status.toUpperCase()));
        jobApplicationRepository.save(app);

        return ResponseEntity.ok(app);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteApplication(@PathVariable UUID id){
        jobApplicationRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
