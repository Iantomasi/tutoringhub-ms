package com.tutoringhub.studentservice.datamapperlayer;


import com.tutoringhub.studentservice.datalayer.Student;
import com.tutoringhub.studentservice.presentationlayer.StudentResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentResponseMapper {

    @Mapping(expression = "java(student.getStudentIdentifier().getStudentId())", target = "studentId")
    StudentResponseModel entityToResponseModel(Student student);
    List<StudentResponseModel> entityListToResponseModelList(List<Student> students);


    @AfterMapping
    default void addLinks(@MappingTarget StudentResponseModel studentResponseModel, Student student) {

        URI baseUri = URI.create("http://localhost:8080");

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "students", studentResponseModel.getStudentId())
                        .toUriString(),
                "self");

        Link clientLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "students")
                        .toUriString(),
                "allStudents");

        studentResponseModel.add(selfLink);
        studentResponseModel.add(clientLink);
    }


}

