package com.tutoringhub.lessonservice.presentationlayer;

import com.tutoringhub.lessonservice.datalayer.Lesson;
import com.tutoringhub.lessonservice.datalayer.LessonStatus;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;


//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class LessonResponseModel extends RepresentationModel<LessonResponseModel> {

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
