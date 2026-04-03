package com.naukri.notification_api.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Company {

    UUID id;

    String companyName;

    String email;

    String webSiteLink;

    String linkedinLink;

    int companySize;

    String industry;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;
}
