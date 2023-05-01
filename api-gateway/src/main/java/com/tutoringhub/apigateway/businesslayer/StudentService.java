package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;

public interface StudentService {

    StudentResponseModel getStudentAggregate(String studentId);


}
