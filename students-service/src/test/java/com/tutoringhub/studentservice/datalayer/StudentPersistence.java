package com.tutoringhub.studentservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class StudentPersistence {

    private Student preSavedStudent;

    @Autowired
    StudentRepository studentRepository;

    @BeforeEach
    public void setupTestDb(){
        studentRepository.deleteAll();
        preSavedStudent = studentRepository.save(new Student("Niccolo", "19", "fagioli44@gmail.com", "Champlain College Saint-Lambert"));
    }

    @Test
    public void addNewStudent_shouldSucceed(){

    //arrange
    String expectedStudentName = "Giuliano";
    String expectedStudentAge = "20";
    String expectedStudentEmail = "gman@gmail.com";
    String expectedStudentSchool = "Champlain College Saint-Lambert";

    Student newStudent = new Student(expectedStudentName, expectedStudentAge, expectedStudentEmail, expectedStudentSchool);

    //act
    Student savedStudent = studentRepository.save(newStudent);

    //assert
    assertNotNull(savedStudent);
    assertNotNull(savedStudent.getId());
    assertNotNull(savedStudent.getStudentIdentifier().getStudentId());
    assertEquals(expectedStudentName, savedStudent.getStudentName());
    assertEquals(expectedStudentAge, savedStudent.getStudentAge());
    assertEquals(expectedStudentEmail, savedStudent.getStudentEmail());
    assertEquals(expectedStudentSchool, savedStudent.getStudentSchool());

    }

    @Test
    public void updateStudent_shouldSucceed()
    {
        //arrange
        String updatedStudentName = "Giuliano2";
        String updatedStudentAge = "21";
        String updatedStudentEmail = "gman2@gmail.com";
        String updatedStudentSchool = "Dawson College";

        //act
        Student savedStudent = studentRepository.save(preSavedStudent);

        //assert
        assertNotNull(savedStudent);
        assertThat(savedStudent, samePropertyValuesAs(preSavedStudent));

    }

    @Test
    public void findStudentByStudentIdentifier_StudentId_Successful(){

      //act
      Student found = studentRepository.findStudentByStudentIdentifier_StudentId(preSavedStudent.getStudentIdentifier().getStudentId());

      //assert
      assertNotNull(found);
      assertThat(preSavedStudent, samePropertyValuesAs(found));
    }

    @Test
    public void findStudentByStudentIdentifier_InvalidStudentId_Failed(){
        //act
        Student found = studentRepository.findStudentByStudentIdentifier_StudentId(
              preSavedStudent.getStudentIdentifier().getStudentId()+1);
        //assert
      assertNull(found);
    }

    @Test
    public void findStudentByStudentEmail_Success(){

        //act
        List<Student> found = studentRepository.findStudentByStudentEmail(preSavedStudent.getStudentEmail());

        //assert
        assertEquals(1, found.size());
        assertEquals(preSavedStudent.getStudentName(), found.get(0).getStudentName());
        assertEquals(preSavedStudent.getStudentAge(), found.get(0).getStudentAge());
        assertEquals(preSavedStudent.getStudentEmail(), found.get(0).getStudentEmail());
        assertEquals(preSavedStudent.getStudentSchool(), found.get(0).getStudentSchool());

    }

    @Test
    public void findStudentByStudentEmail_Failed(){

        //act
        List<Student> found = studentRepository.findStudentByStudentEmail(preSavedStudent.getStudentEmail().toUpperCase());

        //assert
        assertEquals(0,found.size());

    }



}