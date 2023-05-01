package com.tutoringhub.lessonservice.presentationlayer;


import com.tutoringhub.lessonservice.businesslayer.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonController {

    private LessonService lessonService;

    public LessonController(LessonService lessonService){ this.lessonService = lessonService; }

    @GetMapping()
    public List<LessonResponseModel> getLessons(){
        return lessonService.getLessons();
    }

    @GetMapping("/{lessonId}")
    public LessonResponseModel getLessonByLessonId(@PathVariable String lessonId){
        return lessonService.getLessonByLessonId(lessonId);
    }

    @PostMapping()
    ResponseEntity<LessonResponseModel> addLesson(@RequestBody LessonRequestModel lessonRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.addLesson(lessonRequestModel));
    }

    @PutMapping("/{lessonId}")
    ResponseEntity<LessonResponseModel> updateLesson(@RequestBody LessonRequestModel lessonRequestModel, @PathVariable String lessonId){
        return ResponseEntity.ok().body(lessonService.updateLesson(lessonRequestModel, lessonId));
    }

    @DeleteMapping("/{lessonId}")
    ResponseEntity<Void> removeLesson(@PathVariable String lessonId){
        lessonService.removeLesson(lessonId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
