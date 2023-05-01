package com.tutoringhub.tutorservice.presentationlayer;

import com.tutoringhub.tutorservice.datalayer.Experience;
import com.tutoringhub.tutorservice.datalayer.Specialty;
import com.tutoringhub.tutorservice.datalayer.TutorRepository;
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
public class TutorControllerIntegrationTest {

    private final String BASE_URI_TUTORS= "/api/v1/tutors";
    private final String VALID_TUTOR_ID= "c3540a89-cb47-4c96-888e-ff96708db5d4";
    private final String VALID_TUTOR_NAME= "Mourinho";
    private final String VALID_TUTOR_AGE= "22";
    private final String VALID_TUTOR_EMAIL= "chosen1@gmail.com";
    private final double VALID_TUTOR_GPA= 3.7;
    private final String VALID_TUTOR_SPECIALTY = "Math";
    private final String VALID_TUTOR_EXPERIENCE = "Senior";


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    TutorRepository tutorRepository;

    @Test
    public void whenTutorsExist_thenReturnAllStudents(){

        //arrange
        Integer expectedNumberOfTutors = 2;

        //act
        webTestClient.get()
                .uri(BASE_URI_TUTORS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.length()").isEqualTo(expectedNumberOfTutors);

    }

    @Test
    public void whenGetTutorWithValidTutorId_thenReturnTutor(){


        webTestClient.get().uri(BASE_URI_TUTORS + "/" + VALID_TUTOR_ID).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.tutorId").isEqualTo(VALID_TUTOR_ID)
                .jsonPath("$.tutorName").isEqualTo(VALID_TUTOR_NAME)
                .jsonPath("$.tutorAge").isEqualTo(VALID_TUTOR_AGE)
                .jsonPath("$.tutorEmail").isEqualTo(VALID_TUTOR_EMAIL)
                .jsonPath("$.tutorGpa").isEqualTo(VALID_TUTOR_GPA)
                .jsonPath("$.specialty").isEqualTo(VALID_TUTOR_SPECIALTY)
                .jsonPath("$.experience").isEqualTo(VALID_TUTOR_EXPERIENCE);
    }


    @Test
    public void whenGetTutorWithInvalidId_thenReturnNotFoundException(){

        String INVALID_TUTOR_ID = VALID_TUTOR_ID + 1;
        webTestClient.get().uri(BASE_URI_TUTORS + "/" + INVALID_TUTOR_ID).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isEqualTo(HttpStatusCode.valueOf(404))
                .expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody().jsonPath("$.path").isEqualTo("uri=" + BASE_URI_TUTORS + "/" + INVALID_TUTOR_ID)
                .jsonPath("$.message").isEqualTo("No tutor assigned to this tutorId");

    }

    @Test
    public void whenCreateTutorWithValidValues_thenReturnNewTutor(){


        //arrange
        String expectedTutorName = "Giuliano";
        String expectedTutorAge = "20";
        String expectedTutorEmail = "gman@gmail.com";
        double expectedTutorGpa = 3.6;

        TutorRequestModel tutorRequestModel = new TutorRequestModel(expectedTutorName, expectedTutorAge, expectedTutorEmail, expectedTutorGpa, Specialty.French, Experience.MidLevel);
        //act and assert
        webTestClient.post()
                .uri(BASE_URI_TUTORS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tutorRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(TutorResponseModel.class)
                .value((dto) -> {
                    assertNotNull(dto);
                    assertNotNull(dto.getTutorId());
                });
    }


    @Test
    public void whenUpdateTutorWithValidValues_thenReturnUpdatedStudent(){

        //arrange
        String updatedTutorName = "Giuliano2";
        String updatedTutorAge = "20";
        String updatedTutorEmail = "gman@gmail.com";
        double updatedTutorGpa = 3.6;

        TutorRequestModel updatedTutor = new TutorRequestModel(updatedTutorName, updatedTutorAge, updatedTutorEmail, updatedTutorGpa, Specialty.French, Experience.MidLevel);
        //act and assert

        //act and assert
        webTestClient.put()
                .uri(BASE_URI_TUTORS + "/" + VALID_TUTOR_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedTutor)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.tutorId").isNotEmpty()
                .jsonPath("$.tutorName").isEqualTo(updatedTutorName)
                .jsonPath("$.tutorAge").isEqualTo(updatedTutorAge)
                .jsonPath("$.tutorEmail").isEqualTo(updatedTutorEmail)
                .jsonPath("$.tutorGpa").isEqualTo(updatedTutorGpa);
    }

    @Test
    public void whenDeleteTutorByTutorId_thenDeleteTutor(){
        webTestClient.delete().uri(BASE_URI_TUTORS + "/" + VALID_TUTOR_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void whenTutorGpaIsInadequateForPost_thenReturnInadequateGpaException(){

        //arrange
        String expectedTutorName = "Giuliano";
        String expectedTutorAge = "20";
        String expectedTutorEmail = "gman@gmail.com";
        double invalidTutorGpa = 3.1;

        TutorRequestModel tutorRequestModel = new TutorRequestModel(expectedTutorName, expectedTutorAge, expectedTutorEmail, invalidTutorGpa, Specialty.French, Experience.MidLevel);
        //act and assert
        webTestClient.post()
                .uri(BASE_URI_TUTORS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tutorRequestModel)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatusCode.valueOf(422))
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody().jsonPath("$.path")
                .isEqualTo("uri=" + BASE_URI_TUTORS)
                .jsonPath("$.message").isEqualTo("Tutor Gpa must be above 3.5 in order to qualify for giving lessons");

    }

}
