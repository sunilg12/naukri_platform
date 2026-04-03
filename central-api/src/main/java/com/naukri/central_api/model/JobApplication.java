package com.naukri.central_api.model;

import com.naukri.central_api.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobApplication {


    UUID id;
    Job job;
    AppUser applicant;
    FormSubmission formSubmission;
    ApplicationStatus status;
    LocalDateTime appliedAt;
}
