package com.naukri.database_api.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        indexes = {
                @Index(name = "idx_job_location", columnList = "location"),
                @Index(name = "idx_job_exp", columnList = "minExperience, maxExperience"),
                @Index(name = "idx_job_salary", columnList = "minSalary, maxSalary"),
                @Index(name = "idx_job_postedDate", columnList = "postedDate")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    String state;

    @Column(nullable = false)
    String smallDescription;

    @Column(nullable = false)
    String location;

    @Column(nullable = false)
    String jobDescription;

    @OneToOne(mappedBy = "job", cascade = CascadeType.ALL)
    ApplicationForm applicationForm;

    @Column(nullable = false)
    Integer minExperience;

    @Column(nullable = false)
    Integer maxExperience;

    @Column(nullable = false)
    double minSalary;

    @Column(nullable = false)
    double maxSalary;

    @ManyToOne
    AppUser createdBy; // this is the recruiter who created job

    @CreationTimestamp
    LocalDateTime postedDate;

    int totalApplicants;

    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    List<Skill> skills;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    List<JobApplication> jobApplicants;
}
