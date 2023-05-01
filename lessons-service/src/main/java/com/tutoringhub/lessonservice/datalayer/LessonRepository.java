package com.tutoringhub.lessonservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    Lesson findLessonByLessonIdentifier_LessonId(String lessonId);
    //Boolean existsLessonByLessonIdentifier_LessonId(String lessonId);

}
