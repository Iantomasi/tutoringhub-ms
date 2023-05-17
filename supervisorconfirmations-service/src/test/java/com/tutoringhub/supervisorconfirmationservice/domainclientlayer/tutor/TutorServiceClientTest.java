package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.NotFoundException;
import com.tutoringhub.supervisorconfirmationservice.utils.HttpErrorInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TutorServiceClientTest {


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TutorServiceClient tutorServiceClient;

    private String tutorId = "c3540a89-cb47-4c96-888e-ff96708ab1c20";
    private String baseUrl = "http://localhost:8080/api/v1/tutors";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        tutorServiceClient = new TutorServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    public void geTutorByTutorId() {
        TutorResponseModel tutorResponseModel = new TutorResponseModel(tutorId, "Spalletti", "24", "forzamilan@gmail.com", 3.9, Specialty.English, Experience.Junior);
        when(restTemplate.getForObject(baseUrl + "/" + tutorId, TutorResponseModel.class)).thenReturn(tutorResponseModel);

        TutorResponseModel result = tutorServiceClient.getTutorAggregate(tutorId);
        assertEquals(result.getTutorId(), tutorId);
        assertEquals(result.getTutorName(), "Spalletti");
        assertEquals(result.getTutorAge(), "24");
        assertEquals(result.getTutorEmail(), "forzamilan@gmail.com");
        assertEquals(result.getTutorGpa(), 3.9);
        assertEquals(result.getSpecialty(), Specialty.English);
        assertEquals(result.getExperience(), Experience.Junior);


        verify(restTemplate, times(1)).getForObject(baseUrl + "/" + tutorId, TutorResponseModel.class);

    }

    @Test
    public void getTutorByTutorId_NotFoundException() throws JsonProcessingException {
        String tutorId = "This is an invalid tutorId";
        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/tutors/" + tutorId,"Not Found");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String errorInfoJson = "";
        try {
            errorInfoJson = objectMapper.writeValueAsString(errorInfo);
        } catch (JsonProcessingException e) {
            fail("Failed to serialize HttpErrorInfo: " + e.getMessage());
        }

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found",
                HttpHeaders.EMPTY, errorInfoJson.getBytes(), null);

        when(restTemplate.getForObject(baseUrl + "/" + tutorId, TutorResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                tutorServiceClient.getTutorAggregate(tutorId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }


}