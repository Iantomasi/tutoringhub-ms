package com.tutoringhub.apigateway.presentationlayer.student;

import com.tutoringhub.apigateway.businesslayer.StudentService;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping(
            value = "/{studentId}",
            produces = "application/json"
    )
    ResponseEntity<StudentResponseModel> getStudentAggregate(@PathVariable String studentId){
        log.debug("1. Received in Api-Gateway student controller getStudentAggregate with studentId: " + studentId);
        return ResponseEntity.ok().body(studentService.getStudentAggregate(studentId));
    }



}
