package com.tutoringhub.tutorservice.datalayer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TutorPersistence {

    Tutor preSavedTutor;

    @Autowired
    TutorRepository tutorRepository;


    @BeforeEach
    public void setUpTestDb(){
        tutorRepository.deleteAll();
        preSavedTutor = tutorRepository.save(new Tutor("Jerry", "20", "seinfeld@outlook.com", 3.6, Specialty.Math, Experience.Senior));

    }

    @Test
    public void addNewTutor_shouldSucceed(){

        //arrange
        String expectedTutorName = "Iantomasi";
        String expectedTutorAge = "21";
        String expectedTutorEmail= "iantomasig@gmail.com";
        double expectedTutorGpa = 4.0;

        Tutor newTutor = new Tutor(expectedTutorName, expectedTutorAge, expectedTutorEmail, expectedTutorGpa, Specialty.French, Experience.MidLevel);

        //act
        Tutor savedTutor = tutorRepository.save(newTutor);

        //assert
        assertNotNull(savedTutor);
        assertNotNull(savedTutor.getId());
        assertNotNull(savedTutor.getTutorIdentifier().getTutorId());
        assertEquals(expectedTutorName, savedTutor.getTutorName());
        assertEquals(expectedTutorAge, savedTutor.getTutorAge());
        assertEquals(expectedTutorEmail, savedTutor.getTutorEmail());
        assertEquals(expectedTutorGpa, savedTutor.getTutorGpa());

    }

    @Test
    public void updateTutor_shouldSucceed(){

        //arrange
        String updatedTutorName = "Iantomasi4";
        String updatedTutorAge = "21";
        String updatedTutorEmail= "iantomasig@gmail.com";
        double updatedTutorGpa = 4.0;

        Tutor savedTutor = tutorRepository.save(preSavedTutor);
        //assert
        assertNotNull(savedTutor);
        assertThat(savedTutor, samePropertyValuesAs(preSavedTutor));


    }

    @Test
    public void findTutorByTutorIdentifier_TutorId_Successful(){

        //act
        Tutor found = tutorRepository.findTutorByTutorIdentifier_TutorId(preSavedTutor.getTutorIdentifier().getTutorId());

        //assert
        assertNotNull(found);
        assertThat(preSavedTutor, samePropertyValuesAs(found));
    }

    @Test
    public void  findTutorByTutorIdentifier_InvalidTutorId_Failed(){
        //act
        Tutor found = tutorRepository.findTutorByTutorIdentifier_TutorId(preSavedTutor.getTutorIdentifier().getTutorId()+1);
        //assert
        assertNull(found);
    }










}