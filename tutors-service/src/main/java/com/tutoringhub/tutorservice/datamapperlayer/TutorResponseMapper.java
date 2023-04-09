package com.tutoringhub.tutorservice.datamapperlayer;


import com.tutoringhub.tutorservice.datalayer.Tutor;
import com.tutoringhub.tutorservice.presentationlayer.TutorResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TutorResponseMapper {

    @Mapping(expression = "java(tutor.getTutorIdentifier().getTutorId())", target = "tutorId")
    TutorResponseModel entityToResponseModel(Tutor tutor);
    List<TutorResponseModel> entityListToResponseModelList(List<Tutor> tutors);
}
