package com.naukri.central_api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CompanyRegistrationDto {

    String companyName;

    String email;

    String password;

    String websiteLink;

    String linkedinLink;

    Long phoneNumber;

    int companySize;

    String industry;
}
