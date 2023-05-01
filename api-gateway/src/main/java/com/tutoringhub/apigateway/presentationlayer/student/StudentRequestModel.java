package com.tutoringhub.apigateway.presentationlayer.student;

import lombok.*;

@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentRequestModel {

    private String studentName;
    private String studentAge;
    private String studentEmail;
    private String studentSchool;


}
