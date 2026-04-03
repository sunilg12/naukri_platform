package com.naukri.database_api.controllers;

import com.naukri.database_api.dto.JobSearchRequestDto;
import main.com.naukri.database_api.dto.JobSearchRequestDto;
import main.com.naukri.database_api.models.Job;
import main.com.naukri.database_api.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/db/job")
public class JobController {

    JobRepository jobRepository;

    @Autowired
    public  JobController(JobRepository jobRepository){
        this.jobRepository=jobRepository;
    }

    @PostMapping("/save")
    public ResponseEntity saveJob(@RequestBody Job job){
        jobRepository.save(job);
        return new ResponseEntity(job, HttpStatus.CREATED);
    }

//    @PostMapping("/searchJob")
//    public List<Job> searchJob(@RequestBody JobSearchRequestDto request){
//
//        return jobRepository.searchJob(request.getLocation(),
//                request.getMinExperience(),
//                request.getMaxExperience(),
//                request.getMinSalary(),
//                request.getMaxSalary(),
//                request.getSkill());
//    }

    @PostMapping("/searchJob")
    public List<Job> searchJob(@RequestBody JobSearchRequestDto request,
                               @RequestParam(required = false, defaultValue = "1") int pageNo,
                               @RequestParam(required = false, defaultValue = "5") int pageSize){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Job> page = jobRepository.searchJob(request.getLocation(),
                request.getMinExperience(),
                request.getMaxExperience(),
                request.getMinSalary(),
                request.getMaxSalary(),
                request.getSkill(),
                pageable);

        return page.getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id){
        Job job = jobRepository.findById(id).orElse(null);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity updateJob(@RequestBody Job job){
        jobRepository.save(job);
        return new ResponseEntity(job, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable UUID id){
        jobRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
