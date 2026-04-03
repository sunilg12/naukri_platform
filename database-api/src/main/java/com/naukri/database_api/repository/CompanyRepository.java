package com.naukri.database_api.repository;


import main.com.naukri.database_api.models.AppUser;
import com.naukri.database_api.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query(""" 
            select a
            from AppUser a
            join a.skillSet s
            s.name = :name
            """)
    public List<AppUser> getUserBySkills(String skill);

    @Query("""
            SELECT ja.applicant
            FROM JobApplication ja
            JOIN ja.job j
            JOIN j.createdBy u
            where u.company.name = :name
            AND j.id = :jobId
            """)
    public List<AppUser> getUsersBasedOnCompanyAndJobId(String name, UUID jobId);
}
