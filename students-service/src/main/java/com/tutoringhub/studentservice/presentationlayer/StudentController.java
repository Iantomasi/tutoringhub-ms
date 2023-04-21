package com.tutoringhub.studentservice.presentationlayer;

import com.tutoringhub.studentservice.businesslayer.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public List<StudentResponseModel> getStudents(){ return studentService.getStudents(); }

    @GetMapping("/{studentId}")
    public StudentResponseModel getStudentByStudentId(@PathVariable String studentId){
        return studentService.getStudentByStudentId(studentId);
    }

    @PostMapping()
    ResponseEntity<StudentResponseModel> addStudent(@RequestBody StudentRequestModel studentRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.addStudent(studentRequestModel));
    }


    @PutMapping("/{studentId}")
    ResponseEntity<StudentResponseModel> updateStudent(@RequestBody StudentRequestModel studentRequestModel, @PathVariable String studentId){
        return ResponseEntity.ok().body(studentService.updateStudent(studentRequestModel, studentId));
    }

    @DeleteMapping("/{studentId}")
    ResponseEntity<Void>removeStudent(@PathVariable String studentId){
        studentService.removeStudent(studentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
