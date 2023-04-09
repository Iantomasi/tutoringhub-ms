package com.tutoringhub.lessonservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class LessonIdentifier {

    private String lessonId;

    LessonIdentifier(){ this.lessonId = UUID.randomUUID().toString(); }

    public String getLessonId(){ return lessonId; }


}
