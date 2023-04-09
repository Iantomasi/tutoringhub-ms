package com.tutoringhub.studentservice.presentationlayer;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentRequestModel {


    private String studentName;
    private String studentAge;
    private String studentEmail;
    private String studentSchool;
}
