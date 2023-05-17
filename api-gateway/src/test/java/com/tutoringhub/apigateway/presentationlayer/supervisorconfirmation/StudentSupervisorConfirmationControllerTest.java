package com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tutoringhub.apigateway.domainclientlayer.SupervisorConfirmationServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;



import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentSupervisorConfirmationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    SupervisorConfirmationServiceClient supervisorConfirmationServiceClient;

    @Autowired
    private MockMvc mockMvc;


    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrl = "http://localhost:";
    private String studentId = "studentId";

    @BeforeEach
    public void setUp() {
        baseUrl = baseUrl + port + "api/v1/students/";
    }

    @Test
    void getStudentExtraCreditReport() throws Exception{
        String studentId = "studentId";
        List<SupervisorConfirmationResponseModel> confirmations = List.of(new SupervisorConfirmationResponseModel[]{});

        when(supervisorConfirmationServiceClient.getStudentExtraCreditReport(studentId)).thenReturn(confirmations);

        mockMvc.perform(get("/api/v1/students/" + studentId + "/supervisorconfirmations"))
                .andExpect(status().isOk());

        verify(supervisorConfirmationServiceClient, times(1)).getStudentExtraCreditReport(studentId);
    }


    @Test
    void getSupervisorConfirmationInStudentExtraCreditReportById() throws Exception{

        String studentId = "studentId";
        String supervisorConfirmationId = "supervisorConfirmationId";
        SupervisorConfirmationResponseModel supervisorConfirmationResponseModel = new SupervisorConfirmationResponseModel();

        when(supervisorConfirmationServiceClient.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId,supervisorConfirmationId)).thenReturn(supervisorConfirmationResponseModel);

        mockMvc.perform(get("/api/v1/students/" + studentId + "/supervisorconfirmations/" + supervisorConfirmationId))
                .andExpect(status().isOk());

        verify(supervisorConfirmationServiceClient, times(1)).getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId,supervisorConfirmationId);
    }


    @Test
    void processSupervisorConfirmation() throws Exception{
        String studentId = "studentId";

        objectMapper.registerModule(new JavaTimeModule());

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

        when(supervisorConfirmationServiceClient.processStudentExtraCreditSupervisorConfirmation(confirmationRequestModel, studentId)).thenReturn(confirmation1);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/students/" + studentId + "/supervisorconfirmations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmationRequestModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        SupervisorConfirmationResponseModel actual = objectMapper.readValue(responseContent, SupervisorConfirmationResponseModel.class);

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

    }


    @Test
    void updateSupervisorConfirmation() throws Exception{

        String studentId = "studentId";
        String supervisorConfirmationId = "supervisorConfirmationId";

        objectMapper.registerModule(new JavaTimeModule());

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

        when(supervisorConfirmationServiceClient.updateSupervisorConfirmation(confirmationRequestModel,studentId,supervisorConfirmationId)).thenReturn(confirmation1);

        mockMvc.perform(put("/api/v1/students/" + studentId + "/supervisorconfirmations/" + supervisorConfirmationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(confirmationRequestModel)))
                .andExpect(status().isNoContent())
                .andReturn();

        verify(supervisorConfirmationServiceClient, times(1)).updateSupervisorConfirmation(confirmationRequestModel,studentId,supervisorConfirmationId);
    }


    @Test
    void deletePurchaseByCustomerIdAndPurchaseId() throws Exception{


        String studentId = "studentId";
        String supervisorConfirmationId = "supervisorConfirmationId";

        doNothing().when(supervisorConfirmationServiceClient).removeStudentSupervisorConfirmation(studentId,supervisorConfirmationId);

        mockMvc.perform(delete("/api/v1/students/" + studentId + "/supervisorconfirmations/" + supervisorConfirmationId))
                .andExpect(status().isNoContent());

        verify(supervisorConfirmationServiceClient, times(1)).removeStudentSupervisorConfirmation(studentId,supervisorConfirmationId);
    }




}