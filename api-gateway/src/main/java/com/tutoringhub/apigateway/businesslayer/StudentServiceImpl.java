package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.domainclientlayer.StudentServiceClient;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService{


    private StudentServiceClient studentServiceClient;

    public StudentServiceImpl(StudentServiceClient studentServiceClient) {
        this.studentServiceClient = studentServiceClient;
    }

    @Override
    public StudentResponseModel getStudentAggregate(String studentId) {
        return studentServiceClient.getStudentAggregate(studentId);
    }
}
