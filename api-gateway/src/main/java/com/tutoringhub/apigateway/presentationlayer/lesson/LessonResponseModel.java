package com.tutoringhub.apigateway.presentationlayer.lesson;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LessonResponseModel {

    private String lessonId;
    private String lessonSubject;
    private String lessonDate;
    private String lessonDuration;
    private String lessonClassroom;
    private String streetAddress;
    private String city;
    private String postalCode;

}
