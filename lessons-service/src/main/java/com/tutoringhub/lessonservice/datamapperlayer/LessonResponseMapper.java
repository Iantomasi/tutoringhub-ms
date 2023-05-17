package com.tutoringhub.lessonservice.datamapperlayer;


import com.tutoringhub.lessonservice.datalayer.Lesson;
import com.tutoringhub.lessonservice.presentationlayer.LessonResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonResponseMapper {

   @Mapping(expression = "java(lesson.getLessonIdentifier().getLessonId())", target = "lessonId")
   @Mapping(expression = "java(lesson.getAddress().getStreetAddress())", target = "streetAddress")
   @Mapping(expression = "java(lesson.getAddress().getCity())", target = "city")
   @Mapping(expression = "java(lesson.getAddress().getPostalCode())", target = "postalCode")
   LessonResponseModel entityToResponseModel(Lesson lesson);
   List<LessonResponseModel> entityListToResponseModelList(List<Lesson> lessons);


   @AfterMapping
   default void addLinks(@MappingTarget LessonResponseModel lessonResponseModel, Lesson lesson) {

      URI baseUri = URI.create("http://localhost:8080");

      Link selfLink = Link.of(
              ServletUriComponentsBuilder
                      .fromUri(baseUri)
                      .pathSegment("api", "v1", "lessons", lessonResponseModel.getLessonId())
                      .toUriString(),
              "self");

      Link clientLink = Link.of(
              ServletUriComponentsBuilder
                      .fromUri(baseUri)
                      .pathSegment("api", "v1", "lessons")
                      .toUriString(),
              "allLessons");

      lessonResponseModel.add(selfLink);
      lessonResponseModel.add(clientLink);
   }


}



