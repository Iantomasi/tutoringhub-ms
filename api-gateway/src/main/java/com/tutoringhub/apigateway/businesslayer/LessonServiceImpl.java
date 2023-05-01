package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.domainclientlayer.LessonServiceClient;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LessonServiceImpl implements LessonService {

    private LessonServiceClient lessonServiceClient;

    public LessonServiceImpl(LessonServiceClient lessonServiceClient) {
        this.lessonServiceClient = lessonServiceClient;
    }

    @Override
    public LessonResponseModel getLessonAggregate(String lessonId) {
        return lessonServiceClient.getLessonAggregate(lessonId);
    }
}
