package com.tutoringhub.studentservice.presentationlayer;

import com.tutoringhub.studentservice.datalayer.Student;
import com.tutoringhub.studentservice.datalayer.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"/data-mysql.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StudentControllerIntegrationTest {

    private final String BASE_URI_STUDENTS= "/api/v1/students";

    private final String VALID_STUDENT_ID= "c3540a89-cb47-4c96-888e-ff96708ab1c2";
    private final String VALID_STUDENT_NAME= "Cristiano";
    private final String VALID_STUDENT_AGE= "20";
    private final String VALID_STUDENT_EMAIL= "cr7@gmail.com";
    private final String VALID_STUDENT_SCHOOL= "Champlain College Saint-Lambert";



    @Autowired
    WebTestClient webTestClient;

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void whenStudentsExist_thenReturnAllStudents() {

        //arrange
        Integer expectedNumberOfStudents = 2;

        //act
        webTestClient.get()
                .uri(BASE_URI_STUDENTS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumberOfStudents);
    }



    @Test
    public void whenGetStudentWithValidStudentId_thenReturnStudent(){


        webTestClient.get().uri(BASE_URI_STUDENTS + "/" + VALID_STUDENT_ID).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.studentId").isEqualTo(VALID_STUDENT_ID)
                .jsonPath("$.studentName").isEqualTo(VALID_STUDENT_NAME)
                .jsonPath("$.studentAge").isEqualTo(VALID_STUDENT_AGE)
                .jsonPath("$.studentEmail").isEqualTo(VALID_STUDENT_EMAIL)
                .jsonPath("$.studentSchool").isEqualTo(VALID_STUDENT_SCHOOL);

    }

    @Test
    public void whenGetStudentWithInvalidId_thenReturnNotFoundException() {


        String INVALID_STUDENT_ID = VALID_STUDENT_ID + 1;
        webTestClient.get().uri(BASE_URI_STUDENTS + "/" + INVALID_STUDENT_ID).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.path").isEqualTo("uri=" + BASE_URI_STUDENTS + "/" + INVALID_STUDENT_ID)
                .jsonPath("$.message").isEqualTo("No student assigned to this studentId");
    }


    @Test
    public void whenCreateStudentWithValidValues_thenReturnNewStudent(){

        //arrange
        String expectedStudentName = "Giuliano";
        String expectedStudentAge = "20";
        String expectedStudentEmail = "gman@gmail.com";
        String expectedStudentSchool = "Champlain College Saint-Lambert";

        StudentRequestModel studentRequestModel = new StudentRequestModel(expectedStudentName, expectedStudentAge, expectedStudentEmail, expectedStudentSchool);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_STUDENTS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.studentId").isNotEmpty()
                .jsonPath("$.studentName").isEqualTo(expectedStudentName)
                .jsonPath("$.studentAge").isEqualTo(expectedStudentAge)
                .jsonPath("$.studentEmail").isEqualTo(expectedStudentEmail)
                .jsonPath("$.studentSchool").isEqualTo(expectedStudentSchool);

    }

    @Test
    public void whenUpdateStudentWithValidValues_thenReturnUpdatedStudent(){

        //arrange
        String updatedStudentName = "Giuliano2";
        String updatedStudentAge = "22";
        String updatedStudentEmail = "gman2@gmail.com";
        String updatedStudentSchool = "Dawson College";

        StudentRequestModel studentRequestModel = new StudentRequestModel(updatedStudentName, updatedStudentAge, updatedStudentEmail, updatedStudentSchool);

        //act and assert
        webTestClient.put()
                .uri(BASE_URI_STUDENTS + "/" + VALID_STUDENT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.studentId").isNotEmpty()
                .jsonPath("$.studentName").isEqualTo(updatedStudentName)
                .jsonPath("$.studentAge").isEqualTo(updatedStudentAge)
                .jsonPath("$.studentEmail").isEqualTo(updatedStudentEmail)
                .jsonPath("$.studentSchool").isEqualTo(updatedStudentSchool);
    }

    @Test
    public void whenDeleteStudentByStudentId_thenDeleteStudent(){

        webTestClient.delete()
                .uri(BASE_URI_STUDENTS + "/" + VALID_STUDENT_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }



















}