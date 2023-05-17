package com.tutoringhub.supervisorconfirmationservice.datamapperlayer;

import com.tutoringhub.supervisorconfirmationservice.datalayer.SupervisorConfirmation;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring")
public interface SupervisorConfirmationResponseModelMapper {


    @Mapping(expression = "java(supervisorConfirmation.getSupervisorConfirmationIdentifier().getSupervisorConfirmationId())", target = "supervisorConfirmationId")
    @Mapping(expression = "java(supervisorConfirmation.getLessonIdentifier().getLessonId())", target = "lessonId")
    @Mapping(expression = "java(supervisorConfirmation.getStudentIdentifier().getStudentId())", target = "studentId")
    @Mapping(expression = "java(supervisorConfirmation.getTutorIdentifier().getTutorId())", target = "tutorId")
    SupervisorConfirmationResponseModel entityToResponseModel(SupervisorConfirmation supervisorConfirmation);

    List<SupervisorConfirmationResponseModel> entityListToResponseModelList(List<SupervisorConfirmation> supervisorConfirmations);

    @AfterMapping
    default void addLinks(@MappingTarget SupervisorConfirmationResponseModel supervisorConfirmationResponseModel, SupervisorConfirmation supervisorConfirmation) {

        URI baseUri = URI.create("http://localhost:8080");

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "students", supervisorConfirmationResponseModel.getStudentId(), "supervisorconfirmations", supervisorConfirmationResponseModel.getSupervisorConfirmationId())
                        .toUriString(),
                "self");

        Link clientLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "students", supervisorConfirmationResponseModel.getStudentId(), "supervisorconfirmations")
                        .toUriString(),
                "allConfirmations");

        supervisorConfirmationResponseModel.add(selfLink);
        supervisorConfirmationResponseModel.add(clientLink);
    }


}
