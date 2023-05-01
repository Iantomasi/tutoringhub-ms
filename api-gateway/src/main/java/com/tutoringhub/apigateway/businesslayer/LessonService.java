package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;

import java.util.List;

public interface LessonService {

    List<LessonResponseModel> getAllLessonsAggregate();
    LessonResponseModel getLessonAggregate(String lessonId);
    LessonResponseModel addLessonAggregate(LessonRequestModel lessonRequestModel);
    LessonResponseModel updateLessonAggregate(LessonRequestModel lessonRequestModel, String lessonId);
    void removeLessonAggregate(String lessonId);


}
