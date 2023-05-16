package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.domainclientlayer.LessonServiceClient;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LessonServiceImpl implements LessonService {

    private LessonServiceClient lessonServiceClient;

    public LessonServiceImpl(LessonServiceClient lessonServiceClient) {
        this.lessonServiceClient = lessonServiceClient;
    }

    @Override
    public List<LessonResponseModel> getAllLessonsAggregate() {
       // log.debug("2. Received in Api-Gateway Lesson Service Impl getAllLessonsAggregate");
        return lessonServiceClient.getAllLessonsAggregate();
    }

    @Override
    public LessonResponseModel getLessonAggregate(String lessonId) {
       // log.debug("2. Received in Api-Gateway Lesson Service Impl getLessonAggregate");
        return lessonServiceClient.getLessonAggregate(lessonId);
    }

    @Override
    public LessonResponseModel addLessonAggregate(LessonRequestModel lessonRequestModel) {
        //log.debug("2. Received in Api-Gateway Lesson Service Impl addLessonAggregate");
        return lessonServiceClient.addLessonAggregate(lessonRequestModel);
    }

    @Override
    public LessonResponseModel updateLessonAggregate(LessonRequestModel lessonRequestModel, String lessonId) {
        //log.debug("2. Received in Api-Gateway Lesson Service Impl updateLessonAggregate");
        return lessonServiceClient.updateLessonAggregate(lessonRequestModel, lessonId);
    }

    @Override
    public void removeLessonAggregate(String lessonId) {
        //log.debug("2. Received in Api-Gateway Lesson Service Impl removeLessonAggregate with lessonId" + lessonId);
        lessonServiceClient.removeLessonAggregate(lessonId);
    }
}
