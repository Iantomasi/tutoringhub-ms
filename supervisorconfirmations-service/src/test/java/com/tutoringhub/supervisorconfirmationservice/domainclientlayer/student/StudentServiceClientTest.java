package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.student;

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
class StudentServiceClientTest {


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private StudentServiceClient studentServiceClient;

    private String studentId = "c3540a89-cb47-4c96-888e-ff96708ab1c20";
    private String baseUrl = "http://localhost:8080/api/v1/students";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        studentServiceClient = new StudentServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    public void getStudentByStudentId() {
        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");
        when(restTemplate.getForObject(baseUrl + "/" + studentId, StudentResponseModel.class)).thenReturn(studentResponseModel);

        StudentResponseModel result = studentServiceClient.getStudentAggregate(studentId);
        assertEquals(result.getStudentId(), studentId);
        assertEquals(result.getStudentName(), "Neymar");
        assertEquals(result.getStudentAge(), "21");
        assertEquals(result.getStudentEmail(), "njr@gmail.com");
        assertEquals(result.getStudentSchool(), "Champlain College Saint-Lambert");


        verify(restTemplate, times(1)).getForObject(baseUrl + "/" + studentId, StudentResponseModel.class);

    }


    @Test
    public void getStudentByStudentId_NotFoundException() throws JsonProcessingException {
        String studentId = "This is an invalid studentId";
        HttpErrorInfo errorInfo = new HttpErrorInfo(HttpStatus.NOT_FOUND, "/api/v1/students/" + studentId, "Not Found");

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

        when(restTemplate.getForObject(baseUrl + "/" + studentId, StudentResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                studentServiceClient.getStudentAggregate(studentId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }



}