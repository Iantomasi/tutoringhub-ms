package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

