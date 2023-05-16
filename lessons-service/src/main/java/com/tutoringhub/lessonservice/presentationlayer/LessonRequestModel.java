package com.tutoringhub.lessonservice.presentationlayer;

import com.tutoringhub.lessonservice.datalayer.LessonStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

