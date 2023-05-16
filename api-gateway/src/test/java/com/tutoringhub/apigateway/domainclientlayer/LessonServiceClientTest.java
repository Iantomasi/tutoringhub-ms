package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonStatus;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.List;

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
    public void getAllLessons_Success() {
        // Arrange
        String url = baseUrl;


        LessonResponseModel lesson1 = new LessonResponseModel("12345","English", "May 15 2023", "14:00-14:45", "A101", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        LessonResponseModel lesson2 = new LessonResponseModel("56789","French", "May 15 2023", "14:00-14:45", "A101", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        LessonResponseModel[] lessonResponseModels = new LessonResponseModel[]{lesson1, lesson2};

        when(restTemplate.getForObject(url, LessonResponseModel[].class)).thenReturn(lessonResponseModels);

        // Act
        List<LessonResponseModel> actualLessonResponseModels = lessonServiceClient.getAllLessonsAggregate();

        // Assert
        assertEquals(lessonResponseModels.length, actualLessonResponseModels.size());
        for (int i = 0; i < lessonResponseModels.length; i++) {
            assertEquals(lessonResponseModels[i], actualLessonResponseModels.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, LessonResponseModel[].class);

    }

    @Test
    void getAllLessons_ShouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(LessonResponseModel[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            try {
                lessonServiceClient.getAllLessonsAggregate();
            } catch (NotFoundException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        });
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
    public void addLessonTest() {
        LessonRequestModel lessonRequestModel = LessonRequestModel.builder()
                .lessonSubject("English")
                .lessonDate("May 15 2023")
                .lessonDuration("14:00-14:45")
                .lessonClassroom("A101")
                .lessonStatus(LessonStatus.SCHEDULED)
                .streetAddress("900 Riverside Drive")
                .city("Saint-Lambert")
                .postalCode("J6V 1S4")
                .city("city")
                .build();

        LessonResponseModel lessonResponseModel = new LessonResponseModel(lessonId,"English", "May 15 2023", "14:00-14:45", "A101", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");

        when(restTemplate.postForObject(baseUrl, lessonRequestModel, LessonResponseModel.class)).thenReturn(lessonResponseModel);

        LessonResponseModel result = lessonServiceClient.addLessonAggregate(lessonRequestModel);

        assertEquals(result.getLessonId(), lessonId);

        verify(restTemplate, times(1)).postForObject(baseUrl, lessonRequestModel, LessonResponseModel.class);
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




    @Test
    public void updateLesson() {
        // Given
        String lessonId = "2032334";

        String url = baseUrl + "/" + lessonId;

        LessonRequestModel lessonRequestModel = new LessonRequestModel("English", "May 15 2023", "14:00-14:45", "A101", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");

        when(restTemplate.execute(eq(url), eq(HttpMethod.PUT), any(RequestCallback.class), any())).thenReturn(null);

        // When
        lessonServiceClient.updateLessonAggregate(lessonRequestModel, lessonId);

        // Then
        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.PUT), any(RequestCallback.class), any());
    }

    @Test
    public void removeLesson() {
        // Given
        String lessonId = "2032334";
        String url = baseUrl + "/" + lessonId;

        when(restTemplate.execute(eq(url), eq(HttpMethod.DELETE), any(),  any())).thenReturn(null);

        // When
        lessonServiceClient.removeLessonAggregate(lessonId);

        // Then
        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.DELETE), any(),  any());
    }

    @Test
    public void callbackMethodTest() throws Exception {
        // Arrange
        LessonRequestModel lessonRequestModel = LessonRequestModel.builder()
                .lessonSubject("English")
                .lessonDate("May 15 2023")
                .lessonDuration("14:00-14:45")
                .lessonClassroom("A101")
                .lessonStatus(LessonStatus.SCHEDULED)
                .streetAddress("900 Riverside Drive")
                .city("Saint-Lambert")
                .postalCode("J6V 1S4")
                .city("city")
                .build();

        // Mocking ClientHttpRequest
        ClientHttpRequest clientHttpRequest = mock(ClientHttpRequest.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(clientHttpRequest.getBody()).thenReturn(outputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        when(clientHttpRequest.getHeaders()).thenReturn(httpHeaders);

        // Access private method via reflection
        Method requestCallbackMethod = LessonServiceClient.class.getDeclaredMethod("requestCallback", LessonRequestModel.class);
        requestCallbackMethod.setAccessible(true);

        // Act
        RequestCallback requestCallback = (RequestCallback) requestCallbackMethod.invoke(lessonServiceClient, lessonRequestModel);
        requestCallback.doWithRequest(clientHttpRequest);

        // Assert
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(lessonRequestModel);
        String actualBody = outputStream.toString();
        assertEquals(expectedBody, actualBody);

        assertEquals(MediaType.APPLICATION_JSON_VALUE, httpHeaders.getContentType().toString());
        assertTrue(httpHeaders.getAccept().contains(MediaType.APPLICATION_JSON));
    }

}
