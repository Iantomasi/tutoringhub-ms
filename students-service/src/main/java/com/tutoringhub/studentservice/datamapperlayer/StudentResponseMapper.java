package com.tutoringhub.studentservice.datamapperlayer;


import com.tutoringhub.studentservice.datalayer.Student;
import com.tutoringhub.studentservice.presentationlayer.StudentResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentResponseMapper {

    @Mapping(expression = "java(student.getStudentIdentifier().getStudentId())", target = "studentId")
    StudentResponseModel entityToResponseModel(Student student);
    List<StudentResponseModel> entityListToResponseModelList(List<Student> students);

}

