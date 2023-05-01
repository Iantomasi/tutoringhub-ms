package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.presentationlayer.student.StudentRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;

import java.util.List;

public interface StudentService {

    List<StudentResponseModel> getAllStudentsAggregate();
    StudentResponseModel getStudentAggregate(String studentId);
    StudentResponseModel addStudentAggregate(StudentRequestModel studentRequestModel);
    StudentResponseModel updateStudentAggregate(StudentRequestModel studentRequestModel, String studentId);
    void removeStudentAggregate(String studentId);
}
