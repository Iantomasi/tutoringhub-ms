package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.Approval;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationRequestModel;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SupervisorConfirmationServiceClientTest {


    @MockBean
    private RestTemplate restTemplate;

    @Autowired
    private SupervisorConfirmationServiceClient supervisorConfirmationServiceClient;

    private String studentId = "4d16bb8e-5d02-443c-9112-9661282befe2";

    private String baseStudentSupervisorConfirmationUrl = "http://localhost:8080/api/v1/students/"+ studentId +"/supervisorconfirmations";


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        supervisorConfirmationServiceClient = new SupervisorConfirmationServiceClient(restTemplate, new ObjectMapper(), "localhost", "8080");
    }

    @Test
    public void getStudentExtraCreditReportTest() {
        String url = baseStudentSupervisorConfirmationUrl;

        SupervisorConfirmationResponseModel confirmation1 = new SupervisorConfirmationResponseModel("012", "345", "678", "910", "English", "Messi",
                "Ronaldo", Approval.PENDING, LocalDate.of(2023, 04, 10), "To be evaluated");


        SupervisorConfirmationResponseModel confirmation2 = new SupervisorConfirmationResponseModel("111", "222", "333", "444", "French", "Mbappe",
                "Neymar", Approval.PENDING, LocalDate.of(2023, 05, 10), "To be evaluated");
        List<SupervisorConfirmationResponseModel> confirmations = List.of(confirmation1, confirmation2);

        when(restTemplate.getForObject(url, SupervisorConfirmationResponseModel[].class)).thenReturn(confirmations.toArray(new SupervisorConfirmationResponseModel[0]));

        List<SupervisorConfirmationResponseModel> actual = supervisorConfirmationServiceClient.getStudentExtraCreditReport(studentId);

        assertEquals(confirmations.size(), actual.size());
        for (int i = 0; i < confirmations.size(); i++) {
            assertEquals(confirmations.get(i), actual.get(i));
        }

        verify(restTemplate, times(1)).getForObject(url, SupervisorConfirmationResponseModel[].class);
    }



    @Test
    public void getSupervisorConfirmationInStudentExtraCreditReportBySupervisorConfirmationIdTest() {
        String supervisorConfirmationId = "supervisorConfirmationId";

        String url = baseStudentSupervisorConfirmationUrl + "/" + supervisorConfirmationId;
        SupervisorConfirmationResponseModel confirmation1 = new SupervisorConfirmationResponseModel("supervisorConfirmationId", "345", "678", "910", "English", "Messi",
                "Ronaldo", Approval.PENDING, LocalDate.of(2023, 04, 10), "To be evaluated");

        when(restTemplate.getForObject(url, SupervisorConfirmationResponseModel.class)).thenReturn(confirmation1);

        SupervisorConfirmationResponseModel actualConfirmation = supervisorConfirmationServiceClient.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId,supervisorConfirmationId);

        assertEquals(confirmation1, actualConfirmation);

        verify(restTemplate, times(1)).getForObject(url, SupervisorConfirmationResponseModel.class);
    }

    @Test
    public void getSupervisorConfirmationInStudentExtraCreditReportBySupervisorConfirmationIdTest_ThrowsException() {

        String supervisorConfirmationId = "supervisorConfirmationId";

        String url = baseStudentSupervisorConfirmationUrl + "/" + supervisorConfirmationId;

        HttpErrorInfo errorInfo = new HttpErrorInfo( HttpStatus.NOT_FOUND, "/api/v1/students" + studentId + "/supervisorconfirmations/" + supervisorConfirmationId,"Not Found");

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

        when(restTemplate.getForObject(url, SupervisorConfirmationResponseModel.class)).thenThrow(ex);

        Exception exception = assertThrows(NotFoundException.class, () ->
                supervisorConfirmationServiceClient.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId,supervisorConfirmationId));

        assertTrue(exception.getMessage().contains("Not Found"));
    }


    @Test
    public void addSupervisorConfirmationTest() {

        SupervisorConfirmationRequestModel confirmationRequestModel = SupervisorConfirmationRequestModel.builder()
                .supervisorConfirmationId("supervisorConfirmationId")
                .lessonId("b7024d89-1a5e-4517-3gba-05178u7ar260")
                .studentId("c3540a89-cb47-4c96-888e-ff96708ab1c3")
                .tutorId("c3540a89-cb47-4c96-888e-ff96708db5d5")
                .lessonSubject("English")
                .studentName("Leo")
                .tutorName("Messi")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 06, 17))
                .supervisorComments("To be evaluated")
                .build();


        SupervisorConfirmationResponseModel confirmation1 = new SupervisorConfirmationResponseModel("supervisorConfirmationId", "b7024d89-1a5e-4517-3gba-05178u7ar260", "c3540a89-cb47-4c96-888e-ff96708ab1c3", "c3540a89-cb47-4c96-888e-ff96708db5d5", "English", "Leo",
                "Messi", Approval.PENDING, LocalDate.of(2023, 06, 17), "To be evaluated");

        String expectedUrl = "http://localhost:8080/api/v1/students/4d16bb8e-5d02-443c-9112-9661282befe2/supervisorconfirmations";

        when(restTemplate.postForObject(eq(expectedUrl), any(HttpEntity.class), eq(SupervisorConfirmationResponseModel.class))).thenReturn(confirmation1);

        SupervisorConfirmationResponseModel actual = supervisorConfirmationServiceClient.processStudentExtraCreditSupervisorConfirmation(confirmationRequestModel, studentId);

        assertNotNull(actual);

        assertEquals(confirmation1.getSupervisorConfirmationId(), actual.getSupervisorConfirmationId() );
        assertEquals(confirmation1.getLessonId(), actual.getLessonId());
        assertEquals(confirmation1.getStudentId(), actual.getStudentId());
        assertEquals(confirmation1.getTutorId(), actual.getTutorId());
        assertEquals(confirmation1.getLessonSubject(), actual.getLessonSubject());
        assertEquals(confirmation1.getStudentName(), actual.getStudentName());
        assertEquals(confirmation1.getTutorName(), actual.getTutorName());
        assertEquals(confirmation1.getApproval(), actual.getApproval());
        assertEquals(confirmation1.getConfirmationDate(), actual.getConfirmationDate());
        assertEquals(confirmation1.getSupervisorComments(), actual.getSupervisorComments());

        verify(restTemplate, times(1)).postForObject(eq(expectedUrl), any(HttpEntity.class), eq(SupervisorConfirmationResponseModel.class));
    }


    @Test
    public void updateStudentSupervisorConfirmationTest() {
        String supervisorConfitmationId = "supervisorConfirmationId";
        String expectedUrl = "http://localhost:8080/api/v1/students/4d16bb8e-5d02-443c-9112-9661282befe2/supervisorconfirmations/" + supervisorConfitmationId;

        SupervisorConfirmationRequestModel confirmationRequestModel = SupervisorConfirmationRequestModel.builder()
                .supervisorConfirmationId("supervisorConfirmationId")
                .lessonId("b7024d89-1a5e-4517-3gba-05178u7ar260")
                .studentId("c3540a89-cb47-4c96-888e-ff96708ab1c3")
                .tutorId("c3540a89-cb47-4c96-888e-ff96708db5d5")
                .lessonSubject("English")
                .studentName("Leo")
                .tutorName("Messi")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 06, 17))
                .supervisorComments("To be evaluated")
                .build();

        //when(restTemplate.put(eq(expectedUrl), any(), any(), any())).thenReturn(null);

        when(restTemplate.getForObject(eq(expectedUrl), eq(SupervisorConfirmationResponseModel.class))).thenReturn(null);

        supervisorConfirmationServiceClient.updateSupervisorConfirmation(confirmationRequestModel, studentId, supervisorConfitmationId);

        //verify(restTemplate, times(1)).put(eq(expectedUrl), any(), any(), any());
        verify(restTemplate, times(1)).getForObject(eq(expectedUrl), eq(SupervisorConfirmationResponseModel.class));
    }

    @Test
    public void removeStudentSupervisorConfirmationTest() {
        String supervisorConfirmationId = "supervisorConfirmationId";
        String expectedUrl = "http://localhost:8080/api/v1/students/4d16bb8e-5d02-443c-9112-9661282befe2/supervisorconfirmations/" + supervisorConfirmationId;

        supervisorConfirmationServiceClient.removeStudentSupervisorConfirmation(studentId,supervisorConfirmationId);

        verify(restTemplate).delete(expectedUrl);

    }


}