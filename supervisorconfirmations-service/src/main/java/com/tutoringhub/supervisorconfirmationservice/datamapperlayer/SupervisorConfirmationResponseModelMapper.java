package com.tutoringhub.supervisorconfirmationservice.datamapperlayer;

import com.tutoringhub.supervisorconfirmationservice.datalayer.SupervisorConfirmation;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SupervisorConfirmationResponseModelMapper {


    @Mapping(expression = "java(supervisorConfirmation.getSupervisorConfirmationIdentifier().getSupervisorConfirmationId())", target = "supervisorConfirmationId")
    @Mapping(expression = "java(supervisorConfirmation.getLessonIdentifier().getLessonId())", target = "lessonId")
    @Mapping(expression = "java(supervisorConfirmation.getStudentIdentifier().getStudentId())", target = "studentId")
    @Mapping(expression = "java(supervisorConfirmation.getTutorIdentifier().getTutorId())", target = "tutorId")
    SupervisorConfirmationResponseModel entityToResponseModel(SupervisorConfirmation supervisorConfirmation);

    List<SupervisorConfirmationResponseModel> entityListToResponseModelList(List<SupervisorConfirmation> supervisorConfirmations);

}
