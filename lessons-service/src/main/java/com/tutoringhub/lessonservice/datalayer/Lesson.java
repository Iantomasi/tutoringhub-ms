package com.tutoringhub.lessonservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lessons")
@Data
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Embedded
    private LessonIdentifier lessonIdentifier;

    private String lessonSubject;
    private String lessonDate;
    private String lessonDuration;
    private String lessonClassroom;

    @Enumerated(EnumType.STRING)
    private LessonStatus lessonStatus;

    @Embedded
    private Address address;

    Lesson(){ this.lessonIdentifier = new LessonIdentifier(); }

    public Lesson(String lessonSubject, String lessonDate, String lessonDuration, String lessonClassroom, LessonStatus lessonStatus, Address address) {
        this.lessonIdentifier = new LessonIdentifier();
        this.lessonSubject = lessonSubject;
        this.lessonDate = lessonDate;
        this.lessonDuration = lessonDuration;
        this.lessonClassroom = lessonClassroom;
        this.lessonStatus = lessonStatus;
        this.address = address;
    }
}
