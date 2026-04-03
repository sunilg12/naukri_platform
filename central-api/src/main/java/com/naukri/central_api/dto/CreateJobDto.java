package com.naukri.central_api.dto;

import com.naukri.central_api.model.Skill;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateJobDto {
    String state;
    String shortDescription;
    String location;
    String jobDescription;
    List<String> skill;
    List<String> questions;
}
