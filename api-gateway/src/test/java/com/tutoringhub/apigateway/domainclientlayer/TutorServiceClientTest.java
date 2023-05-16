package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.Experience;
import com.tutoringhub.apigateway.presentationlayer.tutor.Specialty;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorRequestModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class TutorServiceClientTest {



    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private TutorServiceClient tutorServiceClient;

    private String tutorId = "c3540a89-cb47-4c96-888e-ff96708ab1c20";
    private String baseUrl = "http://localhost:8080/api/v1/students";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        tutorServiceClient = new TutorServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    public void getAllTutors_Success() {
        // Arrange
        String url = baseUrl;


        TutorResponseModel tutor1 = new TutorResponseModel("12345", "Spalletti", "24", "forzamilan@gmail.com", 3.9, Specialty.English, Experience.Junior);
        TutorResponseModel tutor2 = new TutorResponseModel("56789", "Inzaghi", "24", "forzamilan@gmail.com", 3.9, Specialty.English, Experience.Junior);
        TutorResponseModel[] tutorResponseModels = new TutorResponseModel[]{tutor1, tutor2};

        when(restTemplate.getForObject(url, TutorResponseModel[].class)).thenReturn(tutorResponseModels);

        // Act
        List<TutorResponseModel> actualTutorResponseModels = tutorServiceClient.getAllTutorsAggregate();

        // Assert
        assertEquals(tutorResponseModels.length, actualTutorResponseModels.size());
        for (int i = 0; i < tutorResponseModels.length; i++) {
            assertEquals(tutorResponseModels[i], actualTutorResponseModels.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, TutorResponseModel[].class);

    }

    @Test
    void getAllTutors_ShouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(TutorResponseModel[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            try {
                tutorServiceClient.getAllTutorsAggregate();
            } catch (NotFoundException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        });
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
    public void addTutorTest() {
        TutorRequestModel tutorRequestModel = TutorRequestModel.builder()
                .tutorName("Spalletti")
                .tutorAge("24")
                .tutorEmail("forzamilan@gmail.com")
                .tutorGpa(3.9)
                .specialty(Specialty.English)
                .experience(Experience.Junior)
                .build();

        TutorResponseModel tutorResponseModel = new TutorResponseModel(tutorId, "Spalletti", "24", "forzamilan@gmail.com", 3.9, Specialty.English, Experience.Junior);

        when(restTemplate.postForObject(baseUrl, tutorRequestModel, TutorResponseModel.class)).thenReturn(tutorResponseModel);

        TutorResponseModel result = tutorServiceClient.addTutorAggregate(tutorRequestModel);

        assertEquals(result.getTutorId(), tutorId);

        verify(restTemplate, times(1)).postForObject(baseUrl, tutorRequestModel, TutorResponseModel.class);
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




    @Test
    public void updateTutor() {
        // Given
        String studentId = "2032334";

        String url = baseUrl + "/" + tutorId;

        TutorRequestModel tutorRequestModel = new TutorRequestModel("Tuchel", "22", "tuchy@gmail.com", 3.6, Specialty.Math, Experience.Junior);

        when(restTemplate.execute(eq(url), eq(HttpMethod.PUT), any(RequestCallback.class), any())).thenReturn(null);

        // When
        tutorServiceClient.updateTutorAggregate(tutorRequestModel, tutorId);

        // Then
        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.PUT), any(RequestCallback.class), any());
    }

    @Test
    public void removeTutor() {
        // Given
        String tutorId = "2032334";
        String url = baseUrl + "/" + tutorId;

        when(restTemplate.execute(eq(url), eq(HttpMethod.DELETE), any(),  any())).thenReturn(null);

        // When
        tutorServiceClient.removeTutorAggregate(tutorId);

        // Then
        verify(restTemplate, times(1)).execute(eq(url), eq(HttpMethod.DELETE), any(),  any());
    }

    @Test
    public void callbackMethodTest() throws Exception {
        // Arrange
        TutorRequestModel tutorRequestModel = TutorRequestModel.builder()
                .tutorName("Spalletti")
                .tutorAge("24")
                .tutorEmail("forzamilan@gmail.com")
                .tutorGpa(3.9)
                .specialty(Specialty.English)
                .experience(Experience.Junior)
                .build();

        // Mocking ClientHttpRequest
        ClientHttpRequest clientHttpRequest = mock(ClientHttpRequest.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(clientHttpRequest.getBody()).thenReturn(outputStream);
        HttpHeaders httpHeaders = new HttpHeaders();
        when(clientHttpRequest.getHeaders()).thenReturn(httpHeaders);

        // Access private method via reflection
        Method requestCallbackMethod = TutorServiceClient.class.getDeclaredMethod("requestCallback", TutorRequestModel.class);
        requestCallbackMethod.setAccessible(true);

        // Act
        RequestCallback requestCallback = (RequestCallback) requestCallbackMethod.invoke(tutorServiceClient, tutorRequestModel);
        requestCallback.doWithRequest(clientHttpRequest);

        // Assert
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(tutorRequestModel);
        String actualBody = outputStream.toString();
        assertEquals(expectedBody, actualBody);

        assertEquals(MediaType.APPLICATION_JSON_VALUE, httpHeaders.getContentType().toString());
        assertTrue(httpHeaders.getAccept().contains(MediaType.APPLICATION_JSON));
    }



}