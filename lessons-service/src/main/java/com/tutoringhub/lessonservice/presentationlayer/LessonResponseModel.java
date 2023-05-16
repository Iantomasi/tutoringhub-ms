package com.tutoringhub.lessonservice.presentationlayer;

import com.tutoringhub.lessonservice.datalayer.LessonStatus;
import lombok.*;


@Value
@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonResponseModel {

    private String lessonId;
    private String lessonSubject;
    private String lessonDate;
    private String lessonDuration;
    private String lessonClassroom;
    private LessonStatus lessonStatus;
    private String streetAddress;
    private String city;
    private String postalCode;

}
