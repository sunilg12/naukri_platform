package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.dto.JobSeekerRegistrationDto;
import com.naukri.central_api.model.AppUser;
import com.naukri.central_api.model.Skill;
import com.naukri.central_api.utility.AuthUtil;
import com.naukri.central_api.utility.MappingUtility;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Value("${secret.password}")
    String secretPassword;


    SkillService skillService;
    MappingUtility mappingUtility;
    DbApiConnector dbApiConnector;

    @Autowired
    public UserService(SkillService skillService, MappingUtility mappingUtility, DbApiConnector dbApiConnector){
        this.skillService = skillService;
        this.mappingUtility = mappingUtility;
        this.dbApiConnector = dbApiConnector;
    }

    // we need to create model class to map the jobseeker data;
    public AppUser registerJobSeeker(JobSeekerRegistrationDto jobSeekerDto){

        // we need to map jobseekerdto to AppUser
        // we can map here itself or
        //can write mapping class and call here

        List<String> skillNames = jobSeekerDto.getSkillSet();
        List<Skill> skills = skillService.getAllSkills(skillNames);
        AppUser jobSeeker = mappingUtility.mapJobSeekerDetailsToAppUser(jobSeekerDto, skills);
        AppUser user = this.saveAppUser(jobSeeker);
        return user;
    }

    public boolean validateCredentials(String email, String password){
        // we need to call the database api to provide user object based on email
        // to call the database api we should call databaseApi connector
        AppUser user = dbApiConnector.callGetUserByEmailEndpoint(email);
        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

    public String decryptJwtToken(String token){

        String payload = Jwts.parser()
                .setSigningKey(secretPassword)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return payload;
    }

    public AppUser getUserFromToken(String token){
        String email = this.decryptJwtToken(token).split(":")[0];
        return dbApiConnector.callGetUserByEmailEndpoint(email);
    }

    public boolean isAdminUser(AppUser user){
        return user.getUserType().equals("ADMIN");
    }

    public boolean isRecruiter(AppUser user){
        return user.getUserType().equals("RECRUITER");
    }

    public AppUser getUserByEmail(String email){
        return dbApiConnector.callGetUserByEmailEndpoint(email);
    }

    AppUser saveAppUser(AppUser user){
        // this method calls appuser controller of db-Api to save user
        return dbApiConnector.callSaveUserEndpoint(user);
    }

    public List<AppUser> getAppuserBySkillName(Skill name){
        return dbApiConnector.callGetSkillByNameEndpoint(name);
    }
}
