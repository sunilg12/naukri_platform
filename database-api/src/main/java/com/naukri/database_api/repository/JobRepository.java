package com.naukri.database_api.repository;

import com.naukri.database_api.models.Job;
import main.com.naukri.database_api.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    @Query("""
              SELECT DISTINCT j
              FROM Job j
              LEFT JOIN j.skills s
              WHERE (:location IS NULL OR j.location = :location)
              AND (:minExp IS NULL OR j.minExperience >= :minExp)
              AND (:maxExp IS NULL OR j.maxExperience <= :maxExp)
              AND (:minSalary IS NULL OR j.minSalary >= :minSalary)
              AND (:maxSalary IS NULL OR j.maxSalary <= :maxSalary)
              AND (:skill IS NULL OR s.name = :skill)
           """)
    Page<Job> searchJob(@Param("location") String location,
                        @Param("minExp") Integer minExperience,
                        @Param("maxExp") Integer maxExperience,
                        @Param("minSalary") Double minSalary,
                        @Param("maxSalary") Double maxSalary,
                        @Param("skill") String skill,
                        Pageable pageable);
}
