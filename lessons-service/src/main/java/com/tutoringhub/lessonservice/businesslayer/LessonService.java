package com.tutoringhub.lessonservice.businesslayer;

import com.tutoringhub.lessonservice.presentationlayer.LessonRequestModel;
import com.tutoringhub.lessonservice.presentationlayer.LessonResponseModel;

import java.util.List;

public interface LessonService {

    //lesson CRUD
    List<LessonResponseModel> getLessons();
    LessonResponseModel getLessonByLessonId(String lessonId);
    LessonResponseModel addLesson(LessonRequestModel lessonRequestModel);
    LessonResponseModel updateLesson(LessonRequestModel lessonRequestModel, String lessonId);
    void removeLesson(String lessonId);


}
