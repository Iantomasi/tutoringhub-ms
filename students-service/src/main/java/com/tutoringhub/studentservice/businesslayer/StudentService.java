package com.tutoringhub.studentservice.businesslayer;

import com.tutoringhub.studentservice.presentationlayer.StudentRequestModel;
import com.tutoringhub.studentservice.presentationlayer.StudentResponseModel;

import java.util.List;

public interface StudentService {

    List<StudentResponseModel> getStudents();
    StudentResponseModel getStudentByStudentId(String studentId);
    StudentResponseModel addStudent(StudentRequestModel studentRequestModel);
    StudentResponseModel updateStudent(StudentRequestModel studentRequestModel, String studentId);
    void removeStudent(String studentId);

}
