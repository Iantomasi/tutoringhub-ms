package com.tutoringhub.apigateway.presentationlayer.lesson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

//@Value
//@Builder
@Data
@AllArgsConstructor
public class LessonRequestModel {

    private String lessonSubject;
    private String lessonDate;
    private String lessonDuration;
    private String lessonClassroom;
    private LessonStatus lessonStatus;
    private String streetAddress;
    private String city;
    private String postalCode;

}

