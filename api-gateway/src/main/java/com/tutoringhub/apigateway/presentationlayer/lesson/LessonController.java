package com.tutoringhub.apigateway.presentationlayer.lesson;

import com.tutoringhub.apigateway.businesslayer.LessonService;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    private final Integer UUID_SIZE = 36;

    private LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }


    @GetMapping(produces = "application/json")
    ResponseEntity<List<LessonResponseModel>> getAllLessonsAggregate(){
        log.debug("1. Received in API-Gateway Lesson Controller getAllLessonsAggregate");
        return ResponseEntity.ok().body(lessonService.getAllLessonsAggregate());
    }

    @GetMapping(
            value = "/{lessonId}",
            produces = "application/json"
    )
    ResponseEntity<LessonResponseModel>getLessonAggregate(@PathVariable String lessonId){
        log.debug("1. Received in Api-Gateway Lesson Controller getLessonAggregate with lessonId: " + lessonId);
        return ResponseEntity.ok().body(lessonService.getLessonAggregate(lessonId));
    }

    @PostMapping()
    ResponseEntity<LessonResponseModel> addLessonAggregate(@RequestBody LessonRequestModel lessonRequestModel){
        log.debug("1 . Received in Api-Gateway Lesson Controller addLessonAggregate");
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.addLessonAggregate(lessonRequestModel));
    }

    @PutMapping(
            value = "/{lessonId}",
            produces = "application/json"
    )
    ResponseEntity<LessonResponseModel> updateLessonAggregate(@RequestBody LessonRequestModel lessonRequestModel, @PathVariable String lessonId){
        if(lessonId.length() != UUID_SIZE){
            throw new NotFoundException("No lesson assigned to this lessonId");
        }
        log.debug("1. Received in Api-Gateway Lesson Controller updateLessonAggregate with lessonId: " + lessonId);
        return ResponseEntity.ok().body(lessonService.updateLessonAggregate(lessonRequestModel, lessonId));
    }

    @DeleteMapping(
            value = "/{lessonId}",
            produces = "application/json"
    )
    ResponseEntity<Void> removeLessonAggregate(@PathVariable String lessonId){
        if(lessonId.length() != UUID_SIZE){
            throw new NotFoundException("No lesson assigned to this lessonId");
        }
        log.debug("1. Received in Api-Gateway Lesson Controller removeLessonAggregate with lessonId: " + lessonId);

        lessonService.removeLessonAggregate(lessonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
