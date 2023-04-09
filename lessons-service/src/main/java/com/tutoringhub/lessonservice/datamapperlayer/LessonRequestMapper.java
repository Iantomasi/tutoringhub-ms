package com.tutoringhub.lessonservice.datamapperlayer;

import com.tutoringhub.lessonservice.datalayer.Lesson;
import com.tutoringhub.lessonservice.presentationlayer.LessonRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lessonIdentifier", ignore = true)
    Lesson requestModelToEntity(LessonRequestModel lessonRequestModel);

}
