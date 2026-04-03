package com.naukri.central_api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FormSubmissionDto {

    List<AnswerDto> answers;
}
