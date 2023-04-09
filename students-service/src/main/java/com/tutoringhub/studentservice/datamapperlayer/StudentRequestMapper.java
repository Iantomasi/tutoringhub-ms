package com.tutoringhub.studentservice.datamapperlayer;


import com.tutoringhub.studentservice.datalayer.Student;
import com.tutoringhub.studentservice.presentationlayer.StudentRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentRequestMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "studentIdentifier", ignore = true)

    Student requestModelToEntity(StudentRequestModel studentRequestModel);

}