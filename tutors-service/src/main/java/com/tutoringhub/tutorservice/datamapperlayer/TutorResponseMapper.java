package com.tutoringhub.tutorservice.datamapperlayer;


import com.tutoringhub.tutorservice.datalayer.Tutor;
import com.tutoringhub.tutorservice.presentationlayer.TutorResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TutorResponseMapper {

    @Mapping(expression = "java(tutor.getTutorIdentifier().getTutorId())", target = "tutorId")
    TutorResponseModel entityToResponseModel(Tutor tutor);
    List<TutorResponseModel> entityListToResponseModelList(List<Tutor> tutors);

    @AfterMapping
    default void addLinks(@MappingTarget TutorResponseModel tutorResponseModel, Tutor tutor) {

        URI baseUri = URI.create("http://localhost:8080");

        Link selfLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "tutors", tutorResponseModel.getTutorId())
                        .toUriString(),
                "self");

        Link clientLink = Link.of(
                ServletUriComponentsBuilder
                        .fromUri(baseUri)
                        .pathSegment("api", "v1", "tutors")
                        .toUriString(),
                "allTutors");

        tutorResponseModel.add(selfLink);
        tutorResponseModel.add(clientLink);
    }


}
