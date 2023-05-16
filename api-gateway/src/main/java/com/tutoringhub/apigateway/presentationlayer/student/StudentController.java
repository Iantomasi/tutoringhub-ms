package com.tutoringhub.apigateway.presentationlayer.student;

import com.tutoringhub.apigateway.businesslayer.StudentService;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private final Integer UUID_SIZE = 36;

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping(produces = "application/json")
    ResponseEntity<List<StudentResponseModel>> getAllStudentsAggregate(){
       // log.debug("1. Received in API-Gateway Student Controller getAllStudentsAggregate");
        return ResponseEntity.ok().body(studentService.getAllStudentsAggregate());
    }

    @GetMapping(
            value = "/{studentId}",
            produces = "application/json"
    )
    ResponseEntity<StudentResponseModel> getStudentAggregate(@PathVariable String studentId){
      //  log.debug("1. Received in Api-Gateway student controller getStudentAggregate with studentId: " + studentId);
        return ResponseEntity.ok().body(studentService.getStudentAggregate(studentId));
    }

    @PostMapping()
    ResponseEntity<StudentResponseModel> addStudentAggregate(@RequestBody StudentRequestModel studentRequestModel){
    //    log.debug("1. Received in Api-Gateway Student Controller addStudentAggregate");
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudentAggregate(studentRequestModel));
    }

    @PutMapping(
            value = "/{studentId}",
            produces = "application/json"
    )
    ResponseEntity<StudentResponseModel> updateStudentAggregate(@RequestBody StudentRequestModel studentRequestModel, @PathVariable String studentId){
        if(studentId.length() != UUID_SIZE){
            throw new NotFoundException("No student assigned to this studentId");
        }
    //    log.debug("1. Received in Api-Gateway Student Controller updateStudentAggregate with studentId: " + studentId);
        return ResponseEntity.ok().body(studentService.updateStudentAggregate(studentRequestModel, studentId));
    }

    @DeleteMapping(
            value = "/{studentId}",
            produces = "application/json"
    )
    ResponseEntity<Void> removeStudentAggregate(@PathVariable String studentId){
        if(studentId.length() != UUID_SIZE){
            throw new NotFoundException("No student assigned to this studentId");
        }
     //   log.debug("1. Received in Api-Gateway Student Controller removeStudentAggregate with studentId: " + studentId);

        studentService.removeStudentAggregate(studentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}