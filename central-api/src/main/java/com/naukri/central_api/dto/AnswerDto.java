package com.naukri.central_api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AnswerDto {
    UUID questionId;
    String answer;
}
