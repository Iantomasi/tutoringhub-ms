package com.tutoringhub.apigateway.presentationlayer.lesson;

import com.tutoringhub.apigateway.businesslayer.LessonService;
import com.tutoringhub.apigateway.domainclientlayer.LessonServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    private LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping(
            value = "/{lessonId}",
            produces = "application/json"
    )
    ResponseEntity<LessonResponseModel>getLessonAggregate(@PathVariable String lessonId){
        log.debug("1. Received in Api-Gateway lesson controller getLessonAggregate with lessonId: " + lessonId);
        return ResponseEntity.ok().body(lessonService.getLessonAggregate(lessonId));
    }

}
