package com.tutoringhub.apigateway.presentationlayer.lesson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutoringhub.apigateway.domainclientlayer.LessonServiceClient;
import com.tutoringhub.apigateway.domainclientlayer.TutorServiceClient;
import com.tutoringhub.apigateway.presentationlayer.tutor.Experience;
import com.tutoringhub.apigateway.presentationlayer.tutor.Specialty;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorRequestModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LessonControllerIntegrationTest {

    private String port = "8080/";

    @Autowired
    RestTemplate restTemplate;

    @MockBean
    LessonServiceClient lessonServiceClient;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String baseUrlLessons = "http://localhost:";

    @BeforeEach
    public void setUp() {
        baseUrlLessons = baseUrlLessons + port + "api/v1/lessons";
    }

    @Test
    void getAllLessons() throws Exception {

        LessonResponseModel lesson1 = new LessonResponseModel("12345", "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        LessonResponseModel lesson2 = new LessonResponseModel("56789", "French", "April 21 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        List<LessonResponseModel> lessonResponseModels = List.of(new LessonResponseModel[]{lesson1, lesson2});

        when(lessonServiceClient.getAllLessonsAggregate()).thenReturn(lessonResponseModels);

        mockMvc.perform(get("/api/v1/lessons")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lessonServiceClient, times(1)).getAllLessonsAggregate();
    }

    @Test
    void getLessonByLessonId() throws Exception {
        String lessonId = "b7024d89-1a5e-4517-3gba-05178u7ar261";

        LessonResponseModel lessonResponseModel = new LessonResponseModel(lessonId, "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");

        when(lessonServiceClient.getLessonAggregate(lessonId)).thenReturn(lessonResponseModel);

        mockMvc.perform(get("/api/v1/lessons/" + lessonId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(lessonServiceClient, times(1)).getLessonAggregate(lessonId);
    }

    @Test
    public void returnAllLessons() throws Exception {

        String lessonId = "2032334";
        LessonResponseModel lessonResponseModel = new LessonResponseModel(lessonId, "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");

        when(lessonServiceClient.getLessonAggregate(lessonId)).thenReturn(lessonResponseModel);
        assertEquals(lessonResponseModel.getLessonId(), lessonId);
        assertEquals(lessonResponseModel.getLessonSubject(), "English");
        assertEquals(lessonResponseModel.getLessonDate(), "April 20 2023");
        assertEquals(lessonResponseModel.getLessonDuration(), "8:00-9:30");
        assertEquals(lessonResponseModel.getLessonClassroom(), "B202");
        assertEquals(lessonResponseModel.getLessonStatus(), LessonStatus.SCHEDULED);
        assertEquals(lessonResponseModel.getStreetAddress(), "900 Riverside Drive");
        assertEquals(lessonResponseModel.getCity(), "Saint-Lambert");
        assertEquals(lessonResponseModel.getPostalCode(), "J6V 1S4");

    }

    @Test
    void addLesson() throws Exception {
        String lessonId = "2032334";
        LessonRequestModel lessonRequestModel = LessonRequestModel.builder()
                .lessonSubject("English")
                .lessonDate("April 20 2023")
                .lessonDuration("8:00-9:30")
                .lessonClassroom("B202")
                .lessonStatus(LessonStatus.SCHEDULED)
                .streetAddress("900 Riverside Drive")
                .city("Saint-Lambert")
                .postalCode("J6V 1S4")
                .build();

        LessonResponseModel lessonResponseModel = new LessonResponseModel(lessonId, "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");

        when(lessonServiceClient.addLessonAggregate(lessonRequestModel)).thenReturn(lessonResponseModel);


        MvcResult mvcResult = mockMvc.perform(post("/api/v1/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonResponseModel)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        System.out.println("Response Content: " + responseContent);

        LessonResponseModel result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), LessonResponseModel.class);
        assertEquals(lessonResponseModel.getLessonId(), lessonId);
        assertEquals(lessonResponseModel.getLessonSubject(), "English");
        assertEquals(lessonResponseModel.getLessonDate(), "April 20 2023");
        assertEquals(lessonResponseModel.getLessonDuration(), "8:00-9:30");
        assertEquals(lessonResponseModel.getLessonClassroom(), "B202");
        assertEquals(lessonResponseModel.getLessonStatus(), LessonStatus.SCHEDULED);
        assertEquals(lessonResponseModel.getStreetAddress(), "900 Riverside Drive");
        assertEquals(lessonResponseModel.getCity(), "Saint-Lambert");
        assertEquals(lessonResponseModel.getPostalCode(), "J6V 1S4");
    }

    @Test
    public void updateLesson() throws Exception {

        String lessonId = "b7024d89-1a5e-4517-3gba-05178u7ar261";
        LessonRequestModel lessonRequestModel = LessonRequestModel.builder()
                .lessonSubject("English")
                .lessonDate("April 20 2023")
                .lessonDuration("8:00-9:30")
                .lessonClassroom("B202")
                .lessonStatus(LessonStatus.SCHEDULED)
                .streetAddress("900 Riverside Drive")
                .city("Saint-Lambert")
                .postalCode("J6V 1S4")
                .build();

        LessonResponseModel lessonResponseModel = new LessonResponseModel(lessonId, "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");

        when(lessonServiceClient.updateLessonAggregate(lessonRequestModel,lessonId)).thenReturn(lessonResponseModel);

        mockMvc.perform(put("/api/v1/lessons/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lessonRequestModel)))
                .andExpect(status().isOk())
                .andReturn();

        verify(lessonServiceClient, times(1)).updateLessonAggregate(lessonRequestModel, lessonId);
    }

    @Test
    void deleteTutor() throws Exception {
        String lessonId = "b7024d89-1a5e-4517-3gba-05178u7ar261";

        doNothing().when(lessonServiceClient).removeLessonAggregate(lessonId);

        mockMvc.perform(delete("/api/v1/lessons/" + lessonId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(lessonServiceClient, times(1)).removeLessonAggregate(lessonId);
    }



}