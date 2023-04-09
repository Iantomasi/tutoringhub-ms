package com.tutoringhub.lessonservice.presentationlayer;


import com.tutoringhub.lessonservice.businesslayer.LessonService;
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
    public LessonResponseModel addLesson(@RequestBody LessonRequestModel lessonRequestModel){
        return lessonService.addLesson(lessonRequestModel);
    }

    @PutMapping("/{lessonId}")
    public LessonResponseModel updateLesson(@RequestBody LessonRequestModel lessonRequestModel, @PathVariable String lessonId){
        return lessonService.updateLesson(lessonRequestModel, lessonId);
    }

    @DeleteMapping("/{lessonId}")
    public void removeLesson(@PathVariable String lessonId){
        lessonService.removeLesson(lessonId);
    }

}
