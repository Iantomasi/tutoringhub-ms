package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.domainclientlayer.StudentServiceClient;
import com.tutoringhub.apigateway.presentationlayer.student.StudentRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService{


    private StudentServiceClient studentServiceClient;

    public StudentServiceImpl(StudentServiceClient studentServiceClient) {
        this.studentServiceClient = studentServiceClient;
    }

    @Override
    public List<StudentResponseModel> getAllStudentsAggregate() {
        log.debug("2. Received in Api-Gateway Student Service Impl getAllStudentsAggregate");
        return studentServiceClient.getAllStudentsAggregate();
    }

    @Override
    public StudentResponseModel getStudentAggregate(String studentId) {
        log.debug("2. Received in Api-Gateway Lesson Service Impl getStudentAggregate");
        return studentServiceClient.getStudentAggregate(studentId);
    }

    @Override
    public StudentResponseModel addStudentAggregate(StudentRequestModel studentRequestModel) {
        log.debug("2. Received in Api-Gateway Lesson Service Impl addStudentAggregate");
        return studentServiceClient.addStudentAggregate(studentRequestModel);
    }

    @Override
    public StudentResponseModel updateStudentAggregate(StudentRequestModel studentRequestModel, String studentId) {
        log.debug("2. Received in Api-Gateway Lesson Service Impl updateStudentAggregate");
        return studentServiceClient.updateStudentAggregate(studentRequestModel, studentId);
    }

    @Override
    public void removeStudentAggregate(String studentId) {
        log.debug("2. Received in Api-Gateway Lesson Service Impl removeStudentAggregate");
        studentServiceClient.removeStudentAggregate(studentId);
    }
}
