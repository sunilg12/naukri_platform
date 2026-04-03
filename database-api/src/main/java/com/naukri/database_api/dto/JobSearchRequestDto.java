package com.naukri.database_api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobSearchRequestDto {
    String location;

    Integer minExperience;
    Integer maxExperience;

    String skill;

    double minSalary;
    double maxSalary;
}
