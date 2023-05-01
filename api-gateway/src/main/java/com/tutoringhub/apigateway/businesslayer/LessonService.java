package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;

public interface LessonService {
    LessonResponseModel getLessonAggregate(String lessonId);
}
