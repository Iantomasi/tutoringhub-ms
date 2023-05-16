package com.tutoringhub.lessonservice.presentationlayer;

import com.tutoringhub.lessonservice.datalayer.Address;
import com.tutoringhub.lessonservice.datalayer.Lesson;
import com.tutoringhub.lessonservice.datalayer.LessonRepository;
import com.tutoringhub.lessonservice.datalayer.LessonStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
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
public class LessonControllerIntegrationTest {


    private final String BASE_URI_LESSONS= "/api/v1/lessons";

    private final String VALID_LESSON_ID= "b7024d89-1a5e-4517-3gba-05178u7ar260";
    private final String VALID_LESSON_SUBJECT= "Math";
    private final String VALID_LESSON_DATE= "March 2nd 2023";
    private final String VALID_LESSON_DURATION= "13:00-14:00";
    private final String VALID_LESSON_CLASSROOM= "A101";
    private final String VALID_STREET_ADDRESS = "900 Riverside Drive";
    private final String VALID_CITY = "Saint-Lambert";
    private final String VALID_POSTAL_CODE= "H1S 1J2";

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    LessonRepository lessonRepository;


    @Test
    public void whenLessonsExist_thenReturnAllLessons(){

        //arrange
        Integer expectedNumberOfLessons = 2;

        //act
        webTestClient.get()
                .uri(BASE_URI_LESSONS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumberOfLessons);
    }

    @Test
    public void whenGetLessonWithValidLessonId_thenReturnLesson(){
        webTestClient.get().uri(BASE_URI_LESSONS + "/" + VALID_LESSON_ID).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.lessonId").isEqualTo(VALID_LESSON_ID)
                .jsonPath("$.lessonSubject").isEqualTo(VALID_LESSON_SUBJECT)
                .jsonPath("$.lessonDate").isEqualTo(VALID_LESSON_DATE)
                .jsonPath("$.lessonDuration").isEqualTo(VALID_LESSON_DURATION)
                .jsonPath("$.lessonClassroom").isEqualTo(VALID_LESSON_CLASSROOM)
                .jsonPath("$.streetAddress").isEqualTo(VALID_STREET_ADDRESS)
                .jsonPath("$.city").isEqualTo(VALID_CITY)
                .jsonPath("$.postalCode").isEqualTo(VALID_POSTAL_CODE);

    }

    @Test
    public void whenGetLessonWithInvalidLessonId_thenReturnNotFoundException(){

        String INVALID_LESSON_ID= VALID_LESSON_ID +1;

        webTestClient.get().uri(BASE_URI_LESSONS + "/" + INVALID_LESSON_ID).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.path").isEqualTo("uri=" + BASE_URI_LESSONS + "/" + INVALID_LESSON_ID)
                .jsonPath("$.message").isEqualTo("No lesson assigned to this lessonId");
    }

    @Test
    public void whenCreateLessonWithValidValues_theReturnNewLesson(){

        //arrange
        String expectedLessonSubject = "Science";
        String expectedLessonDate = "April 20th 2023";
        String expectedLessonDuration= "14:00-15:30";
        String expectedLessonClassroom= "A101";
        String expectedStreetAddress = "900 Riverside Drive";
        String expectedCity = "Saint-Lambert";
        String expectedPostalCode = "H1S 1J2";


        LessonRequestModel lessonRequestModel = new LessonRequestModel(expectedLessonSubject, expectedLessonDate, expectedLessonDuration, expectedLessonClassroom, LessonStatus.SCHEDULED, expectedStreetAddress, expectedCity, expectedPostalCode);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_LESSONS)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(lessonRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.lessonId").isNotEmpty()
                .jsonPath("$.lessonSubject").isEqualTo(expectedLessonSubject)
                .jsonPath("$.lessonDate").isEqualTo(expectedLessonDate)
                .jsonPath("$.lessonDuration").isEqualTo(expectedLessonDuration)
                .jsonPath("$.lessonClassroom").isEqualTo(expectedLessonClassroom)
                .jsonPath("$.streetAddress").isEqualTo(expectedStreetAddress)
                .jsonPath("$.city").isEqualTo(expectedCity)
                .jsonPath("$.postalCode").isEqualTo(expectedPostalCode);
    }

    @Test
    public void whenUpdateLessonWithValidValues_thenReturnUpdatedLesson(){

        //arrange
        String updatedLessonSubject = "Math";
        String updatedLessonDate = "April 21st 2023";
        String updatedLessonDuration = "12:00-13:00";
        String updatedLessonClassroom = "A103";
        String updatedStreetAddress = "900 Riverside Drive";
        String updatedCity = "Saint-Lambert";
        String updatedPostalCode = "H1S 1J2";

        LessonRequestModel updatedLesson = new LessonRequestModel(updatedLessonSubject, updatedLessonDate, updatedLessonDuration, updatedLessonClassroom, LessonStatus.COMPLETED, updatedStreetAddress, updatedCity, updatedPostalCode);

        //act and assert
        webTestClient.put()
                .uri(BASE_URI_LESSONS + "/" + VALID_LESSON_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedLesson)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.lessonId").isNotEmpty()
                .jsonPath("$.lessonSubject").isEqualTo(updatedLessonSubject)
                .jsonPath("$.lessonDate").isEqualTo(updatedLessonDate)
                .jsonPath("$.lessonDuration").isEqualTo(updatedLessonDuration)
                .jsonPath("$.lessonClassroom").isEqualTo(updatedLessonClassroom)
                .jsonPath("$.streetAddress").isEqualTo(updatedStreetAddress)
                .jsonPath("$.city").isEqualTo(updatedCity)
                .jsonPath("$.postalCode").isEqualTo(updatedPostalCode);
    }


    @Test
    public void whenDeleteLessonByLessonId_thenDeleteLesson(){

        webTestClient.delete().uri(BASE_URI_LESSONS + "/" + VALID_LESSON_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenLessonSubjectIsNotRegistered_thenReturnUnregisteredLessonException(){

        //arrange
        String invalidLessonSubject = "Art";
        String expectedLessonDate = "April 20th 2023";
        String expectedLessonDuration= "14:00-15:30";
        String expectedLessonClassroom= "A101";
        String expectedStreetAddress = "900 Riverside Drive";
        String expectedCity = "Saint-Lambert";
        String expectedPostalCode = "H1S 1J2";


        LessonRequestModel lessonRequestModel = new LessonRequestModel(invalidLessonSubject, expectedLessonDate, expectedLessonDuration, expectedLessonClassroom, LessonStatus.SCHEDULED, expectedStreetAddress, expectedCity, expectedPostalCode);

        //act and assert
        webTestClient.post()
                .uri(BASE_URI_LESSONS)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(lessonRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_LESSONS)
                .jsonPath("$.message").isEqualTo("The only registered lesson subjects are English, French, Math or Science");

    }



}