package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonStatus;
import com.tutoringhub.apigateway.presentationlayer.student.StudentRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
    public void getAllStudents_Success() {
        // Arrange
        String url = baseUrl;

        StudentResponseModel student1 = new StudentResponseModel("12345", "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");
        StudentResponseModel student2 = new StudentResponseModel("56789", "Mbappe", "21", "mb10@gmail.com", "Champlain College Saint-Lambert");
        StudentResponseModel[] studentResponseModels = new StudentResponseModel[]{student1, student2};

        when(restTemplate.getForObject(url, StudentResponseModel[].class)).thenReturn(studentResponseModels);

        // Act
        List<StudentResponseModel> actualStudentResponseModels = studentServiceClient.getAllStudentsAggregate();

        // Assert
        assertEquals(studentResponseModels.length, actualStudentResponseModels.size());
        for (int i = 0; i < studentResponseModels.length; i++) {
            assertEquals(studentResponseModels[i], actualStudentResponseModels.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, StudentResponseModel[].class);

    }

    @Test
    void getAllStudents_ShouldThrowException() {
        // Arrange
        when(restTemplate.getForObject(anyString(), eq(StudentResponseModel[].class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            try {
                studentServiceClient.getAllStudentsAggregate();
            } catch (NotFoundException e) {
                throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
            }
        });
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
    public void addStudentTest() {
        StudentRequestModel studentRequestModel = StudentRequestModel.builder()
                .studentName("Neymar")
                .studentAge("21")
                .studentEmail("njr@gmail.com")
                .studentSchool("Champlain College Saint-Lambert")
                .build();

        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");

        when(restTemplate.postForObject(baseUrl, studentRequestModel, StudentResponseModel.class)).thenReturn(studentResponseModel);

        StudentResponseModel result = studentServiceClient.addStudentAggregate(studentRequestModel);

        assertEquals(result.getStudentId(), studentId);

        verify(restTemplate, times(1)).postForObject(baseUrl, studentRequestModel, StudentResponseModel.class);
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


    @Test
    public void updateStudent() {
        // Given
        String studentId = "2032334";

        String url = baseUrl + "/" + studentId;

        StudentRequestModel studentRequestModel = new StudentRequestModel("Chiesa", "22", "fedex7@gmail.com", "Champlain College Saint-Lambert");

        studentServiceClient.updateStudentAggregate(studentRequestModel,studentId);

        verify(restTemplate).put(eq(url), eq(studentRequestModel), eq(studentId));
    }

    @Test
    public void removeLesson() {
        // Given
        String studentId = "2032334";
        String url = baseUrl + "/" + studentId;

        studentServiceClient.removeStudentAggregate(studentId);

        verify(restTemplate).delete(url);
    }

}


