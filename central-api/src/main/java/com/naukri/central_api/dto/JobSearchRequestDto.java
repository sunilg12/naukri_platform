package com.naukri.central_api.dto;

import java.util.List;

public class JobSearchRequestDto {

    String location;

    Integer minExperience;
    Integer maxExperience;

    List<String> skills;

    String keyword;

}
