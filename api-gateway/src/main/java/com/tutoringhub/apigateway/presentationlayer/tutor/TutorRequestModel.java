package com.tutoringhub.apigateway.presentationlayer.tutor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TutorRequestModel {

    private String tutorName;
    private String tutorAge;
    private String tutorEmail;
    private double tutorGpa;
    private Specialty specialty;
    private Experience experience;
}
