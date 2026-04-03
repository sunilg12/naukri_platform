package com.naukri.central_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobSeekerRegistrationDto {

    private String name;
    private String email;
    private String password;
    private Long phoneNumber;
    private List<String> skillSet;
}
