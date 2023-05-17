package com.tutoringhub.apigateway.presentationlayer.tutor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutoringhub.apigateway.domainclientlayer.StudentServiceClient;
import com.tutoringhub.apigateway.domainclientlayer.TutorServiceClient;
import com.tutoringhub.apigateway.presentationlayer.student.StudentRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TutorControllerIntegrationTest {

    private String port = "8080/";

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    TutorServiceClient tutorServiceClient;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrlTutors = "http://localhost:";

    @BeforeEach
    public void setUp() {
        baseUrlTutors = baseUrlTutors + port + "api/v1/tutors";
    }

    @Test
    void getAllTutors() throws Exception {

        TutorResponseModel tutor1 = new TutorResponseModel("12345", "Xavi", "26", "viscabarca@gmail.com",4.0, Specialty.English, Experience.MidLevel);
        TutorResponseModel tutor2 = new TutorResponseModel("56789", "Ten-Hag", "21", "mufc@gmail.com", 3.6, Specialty.Math, Experience.MidLevel);
        List<TutorResponseModel> tutorResponseModels = List.of(new TutorResponseModel[]{tutor1, tutor2});

        when(tutorServiceClient.getAllTutorsAggregate()).thenReturn(tutorResponseModels);

        mockMvc.perform(get("/api/v1/tutors")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tutorServiceClient, times(1)).getAllTutorsAggregate();
    }

    @Test
    void getTutorByTutorId() throws Exception {
        String tutorId = "c3540a89-cb47-4c96-888e-ff96708db5d4";

        TutorResponseModel tutorResponseModel = new TutorResponseModel(tutorId, "Ten-Hag", "21", "mufc@gmail.com", 3.6, Specialty.Math, Experience.MidLevel);

        when(tutorServiceClient.getTutorAggregate(tutorId)).thenReturn(tutorResponseModel);

        mockMvc.perform(get("/api/v1/tutors/" + tutorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(tutorServiceClient, times(1)).getTutorAggregate(tutorId);
    }

    @Test
    public void returnAllTutors() throws Exception {

        String tutorId = "2032334";
        TutorResponseModel tutorResponseModel = new TutorResponseModel(tutorId, "Ten-Hag", "21", "mufc@gmail.com", 3.6, Specialty.Math, Experience.MidLevel);

        when(tutorServiceClient.getTutorAggregate(tutorId)).thenReturn(tutorResponseModel);
        assertEquals(tutorResponseModel.getTutorId(), tutorId);
        assertEquals(tutorResponseModel.getTutorName(), "Ten-Hag");
        assertEquals(tutorResponseModel.getTutorAge(), "21");
        assertEquals(tutorResponseModel.getTutorEmail(), "mufc@gmail.com");
        assertEquals(tutorResponseModel.getTutorGpa(), 3.6);
        assertEquals(tutorResponseModel.getSpecialty(), Specialty.Math);
        assertEquals(tutorResponseModel.getExperience(), Experience.MidLevel);

    }

    @Test
    void addTutor() throws Exception {
        String tutorId = "2032334";
        TutorRequestModel tutorRequestModel = TutorRequestModel.builder()
                .tutorName("Ten-Hag")
                .tutorAge("21")
                .tutorEmail("mufc@gmail.com")
                .tutorGpa(3.6)
                .specialty(Specialty.Math)
                .experience(Experience.MidLevel)
                .build();

        TutorResponseModel tutorResponseModel = new TutorResponseModel(tutorId, "Ten-Hag", "21", "mufc@gmail.com", 3.6, Specialty.Math, Experience.MidLevel);

        when(tutorServiceClient.addTutorAggregate(tutorRequestModel)).thenReturn(tutorResponseModel);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/tutors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorResponseModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        TutorResponseModel result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), TutorResponseModel.class);
        assertEquals(tutorResponseModel.getTutorId(), tutorId);
        assertEquals(tutorResponseModel.getTutorName(), "Ten-Hag");
        assertEquals(tutorResponseModel.getTutorAge(), "21");
        assertEquals(tutorResponseModel.getTutorEmail(), "mufc@gmail.com");
        assertEquals(tutorResponseModel.getTutorGpa(), 3.6);
        assertEquals(tutorResponseModel.getSpecialty(), Specialty.Math);
        assertEquals(tutorResponseModel.getExperience(), Experience.MidLevel);
    }

    @Test
    public void updateStudent() throws Exception {

        String tutorId = "c3540a89-cb47-4c96-888e-ff96708db5d4";
        TutorRequestModel tutorRequestModel = TutorRequestModel.builder()
                .tutorName("Ten-Hag")
                .tutorAge("21")
                .tutorEmail("mufc@gmail.com")
                .tutorGpa(3.6)
                .specialty(Specialty.Math)
                .experience(Experience.MidLevel)
                .build();

        TutorResponseModel tutorResponseModel = new TutorResponseModel(tutorId, "Ten-Hag", "21", "mufc@gmail.com", 3.6, Specialty.Math, Experience.MidLevel);

        when(tutorServiceClient.updateTutorAggregate(tutorRequestModel,tutorId)).thenReturn(tutorResponseModel);

        mockMvc.perform(put("/api/v1/tutors/" + tutorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(tutorServiceClient, times(1)).updateTutorAggregate(tutorRequestModel, tutorId);
    }

    @Test
    void deleteTutor() throws Exception {
        String tutorId = "c3540a89-cb47-4c96-888e-ff96708ab1c3";

        doNothing().when(tutorServiceClient).removeTutorAggregate(tutorId);

        mockMvc.perform(delete("/api/v1/tutors/" + tutorId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(tutorServiceClient, times(1)).removeTutorAggregate(tutorId);
    }


}
