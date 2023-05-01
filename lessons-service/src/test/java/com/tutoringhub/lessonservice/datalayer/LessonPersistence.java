package com.tutoringhub.lessonservice.datalayer;

import com.tutoringhub.lessonservice.presentationlayer.LessonRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
public class LessonPersistence {


    private Lesson preSavedLesson;

    @Autowired
    LessonRepository lessonRepository;


    @BeforeEach
    public void setupTestDb() {
        lessonRepository.deleteAll();
        Address preSavedAddress = new Address("3040 Rue Sherbrooke O", "Montreal", "H3Z 1A4");
        preSavedLesson = lessonRepository.save(new Lesson("English", "April 29th 2023", "13:00-14:00", "A121", preSavedAddress));
    }

    @Test
    public void addNewLesson_shouldSucceed() {

        //arrange
        String expectedLessonSubject = "Science";
        String expectedLessonDate = "April 20th 2023";
        String expectedLessonDuration = "14:00-15:30";
        String expectedLessonClassroom = "A101";

        Address newAddress = new Address("900 Riverside Drive", "Saint-Lambert", "H1S 1J2");
        Lesson newLesson = new Lesson(expectedLessonSubject, expectedLessonDate, expectedLessonDuration, expectedLessonClassroom, newAddress);

        Lesson savedLesson = lessonRepository.save(newLesson);

        //assert
        assertNotNull(savedLesson);
        assertNotNull(savedLesson.getId());
        assertNotNull(savedLesson.getLessonIdentifier().getLessonId());
        assertEquals(expectedLessonSubject, savedLesson.getLessonSubject());
        assertEquals(expectedLessonDate, savedLesson.getLessonDate());
        assertEquals(expectedLessonDuration, savedLesson.getLessonDuration());
        assertEquals(expectedLessonClassroom, savedLesson.getLessonClassroom());
    }


    @Test
    public void updateLesson_shouldSucceed() {

        //arrange
        String lessonSubject = "Math";
        String lessonDate = "April 21st 2023";
        String lessonDuration = "12:00-13:00";
        String lessonClassroom = "A102";

        //act
        Lesson savedLesson = lessonRepository.save(preSavedLesson);

        //assert
        assertNotNull(savedLesson);
        assertThat(preSavedLesson, samePropertyValuesAs(preSavedLesson));
    }

    @Test
    public void findLessonByLessonIdentifier_LessonId_Successful() {

        //act
        Lesson found = lessonRepository.findLessonByLessonIdentifier_LessonId(preSavedLesson.getLessonIdentifier().getLessonId());

        //assert
        assertNotNull(found);
        assertThat(preSavedLesson, samePropertyValuesAs(found));

    }

    @Test
    public void findLessonByLessonIdentifier_LessonId_Failed() {

        //act
        Lesson found = lessonRepository.findLessonByLessonIdentifier_LessonId(preSavedLesson.getLessonIdentifier().getLessonId() + 1);

        //assert
        assertNull(found);
    }


}

