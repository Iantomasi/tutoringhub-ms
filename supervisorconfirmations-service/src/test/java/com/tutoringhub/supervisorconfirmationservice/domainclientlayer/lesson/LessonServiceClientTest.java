package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson;

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
class LessonServiceClientTest {

    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private LessonServiceClient lessonServiceClient;

    private String lessonId = "b7024d89-1a5e-4517-3gba-05178u7ar260";
    private String baseUrl = "http://localhost:8080/api/v1/lessons";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        lessonServiceClient = new LessonServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }


    @Test
    public void getLessonByLessonId() {
        LessonResponseModel lessonResponseModel = new LessonResponseModel(lessonId,"English", "May 15 2023", "14:00-14:45", "A101", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        when(restTemplate.getForObject(baseUrl + "/" + lessonId, LessonResponseModel.class)).thenReturn(lessonResponseModel);

        LessonResponseModel result = lessonServiceClient.getLessonAggregate(lessonId);
        assertEquals(result.getLessonId(), lessonId);
        assertEquals(result.getLessonSubject(), "English");
        assertEquals(result.getLessonDate(), "May 15 2023");
        assertEquals(result.getLessonDuration(), "14:00-14:45");
        assertEquals(result.getLessonClassroom(), "A101");
        assertEquals(result.getLessonStatus(), LessonStatus.SCHEDULED);
        assertEquals(result.getStreetAddress(), "900 Riverside Drive");
        assertEquals(result.getCity(), "Saint-Lambert");
        assertEquals(result.getPostalCode(), "J6V 1S4");


        verify(restTemplate, times(1)).getForObject(baseUrl + "/" + lessonId, LessonResponseModel.class);

    }


    @Test
    public void getLessonByLessonId_NotFoundException() throws JsonProcessingException {
        String lessonId = "This is an invalid lessonId";
        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/lessons/" + lessonId,"Not Found");

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

        when(restTemplate.getForObject(baseUrl + "/" + lessonId, LessonResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                lessonServiceClient.getLessonAggregate(lessonId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }






}