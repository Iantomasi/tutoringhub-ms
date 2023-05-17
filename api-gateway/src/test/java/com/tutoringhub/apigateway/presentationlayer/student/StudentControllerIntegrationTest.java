package com.tutoringhub.apigateway.presentationlayer.student;
import com.tutoringhub.apigateway.domainclientlayer.StudentServiceClient;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {


    private String port = "8080/";

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    StudentServiceClient studentServiceClient;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrlStudents = "http://localhost:";

    @BeforeEach
    public void setUp() {
        baseUrlStudents = baseUrlStudents + port + "api/v1/students";
    }

    @Test
    void getAllStudents() throws Exception {

        StudentResponseModel student1 = new StudentResponseModel("12345", "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");
        StudentResponseModel student2 = new StudentResponseModel("56789", "Mbappe", "21", "mb10@gmail.com", "Champlain College Saint-Lambert");
        List<StudentResponseModel> studentResponseModels = List.of(new StudentResponseModel[]{student1, student2});

        when(studentServiceClient.getAllStudentsAggregate()).thenReturn(studentResponseModels);

        mockMvc.perform(get("/api/v1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentServiceClient, times(1)).getAllStudentsAggregate();
    }

    @Test
    void getStudentByStudentId() throws Exception {
        String studentId = "2032334";

        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");

        when(studentServiceClient.getStudentAggregate(studentId)).thenReturn(studentResponseModel);

        mockMvc.perform(get("/api/v1/students/" + studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(studentServiceClient, times(1)).getStudentAggregate(studentId);
    }

    @Test
    public void returnAllStudents() throws Exception {

        String studentId = "2032334";
        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");

        when(studentServiceClient.getStudentAggregate(studentId)).thenReturn(studentResponseModel);
        assertEquals(studentResponseModel.getStudentId(), studentId);
        assertEquals(studentResponseModel.getStudentName(), "Neymar");
        assertEquals(studentResponseModel.getStudentAge(), "21");
        assertEquals(studentResponseModel.getStudentEmail(), "njr@gmail.com");
        assertEquals(studentResponseModel.getStudentSchool(), "Champlain College Saint-Lambert");
    }

    @Test
    void addStudent() throws Exception {
        String studentId = "2032334";
        StudentRequestModel studentRequestModel = StudentRequestModel.builder()
                .studentName("Neymar")
                .studentAge("21")
                .studentEmail("njr@gmail.com")
                .studentSchool("Champlain College Saint-Lambert")
                .build();

        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");

        when(studentServiceClient.addStudentAggregate(studentRequestModel)).thenReturn(studentResponseModel);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentResponseModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        StudentResponseModel result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), StudentResponseModel.class);
        assertEquals(studentResponseModel.getStudentId(), studentId);
        assertEquals(studentResponseModel.getStudentName(), "Neymar");
        assertEquals(studentResponseModel.getStudentAge(), "21");
        assertEquals(studentResponseModel.getStudentEmail(), "njr@gmail.com");
        assertEquals(studentResponseModel.getStudentSchool(), "Champlain College Saint-Lambert");
    }

    @Test
    public void updateStudent() throws Exception {

        String studentId = "c3540a89-cb47-4c96-888e-ff96708ab1c3";
        StudentRequestModel studentRequestModel = StudentRequestModel.builder()
                .studentName("Neymar")
                .studentAge("21")
                .studentEmail("njr@gmail.com")
                .studentSchool("Champlain College Saint-Lambert")
                .build();

        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");

        when(studentServiceClient.updateStudentAggregate(studentRequestModel,studentId)).thenReturn(studentResponseModel);

        mockMvc.perform(put("/api/v1/students/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(studentServiceClient, times(1)).updateStudentAggregate(studentRequestModel, studentId);
    }

    @Test
    void deleteStudent() throws Exception {
        String studentId = "c3540a89-cb47-4c96-888e-ff96708ab1c3";

        doNothing().when(studentServiceClient).removeStudentAggregate(studentId);

        mockMvc.perform(delete("/api/v1/students/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(studentServiceClient, times(1)).removeStudentAggregate(studentId);
    }


}