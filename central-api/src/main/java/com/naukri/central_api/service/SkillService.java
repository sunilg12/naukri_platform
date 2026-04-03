package com.naukri.central_api.service;

import com.naukri.central_api.connectors.DbApiConnector;
import com.naukri.central_api.model.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SkillService {

    DbApiConnector dbApiConnector;

    @Autowired
    public  SkillService(DbApiConnector dbApiConnector){
        this.dbApiConnector = dbApiConnector;
    }

    public List<Skill> getAllSkills(List<String> skillNames){
        List<Skill> skillObj = new ArrayList<>();
        for(int i=0; i<skillNames.size(); i++){
            String skillName = skillNames.get(i);
            // we need to get skill object from database api by skill
            Skill skill = this.getSkillName(skillName);
            skillObj.add(skill);
        }
        return skillObj;
    }

    public Skill createSkillName(String skillName){
        Skill skill = new Skill();
        skill.setName(skillName);

        return dbApiConnector.callSaveSkillEndpoint(skill);
    }

    public Skill getSkillName(String skillName){
        // this function make call to dbApi skill controller and bring skill object from databse
       Skill skill =  dbApiConnector.callGetSkillByNameEndpoint(skillName);
       if(skill == null){
           return this.createSkillName(skillName);
       }
       return skill;
    }
}
