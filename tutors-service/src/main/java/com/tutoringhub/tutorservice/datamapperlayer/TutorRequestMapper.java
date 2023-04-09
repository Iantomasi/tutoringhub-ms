package com.tutoringhub.tutorservice.datamapperlayer;


import com.tutoringhub.tutorservice.datalayer.Tutor;
import com.tutoringhub.tutorservice.datalayer.TutorIdentifier;
import com.tutoringhub.tutorservice.presentationlayer.TutorRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TutorRequestMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target= "tutorIdentifier", ignore = true)

    Tutor requestModelToEntity(TutorRequestModel tutorRequestModel);
}
