package com.naukri.central_api.dto;

import lombok.*;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class  RecruiterDetailsDto {
    private String name;
    private String email;
    private Long phoneNumber;
}
