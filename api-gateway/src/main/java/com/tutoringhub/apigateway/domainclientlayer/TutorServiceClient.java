package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.InadequateGpaException;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import com.tutoringhub.apigateway.utils.exceptions.UnregisteredLessonSubjectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@Component
public class TutorServiceClient {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String TUTOR_SERVICE_BASE_URL;

    public TutorServiceClient(RestTemplate restTemplate,
                               ObjectMapper objectMapper,
                               @Value("${app.tutors-service.host}") String tutorsServiceHost,
                               @Value("{app.tutors-service.port}") String tutorsServicePort){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.TUTOR_SERVICE_BASE_URL = "http://" + tutorsServiceHost + ":" + tutorsServicePort + "api/v1/tutors";
    }



    public TutorResponseModel getTutorAggregate(String tutorId){

        TutorResponseModel tutorResponseModel;
        try {
            String url = TUTOR_SERVICE_BASE_URL + "/" + tutorId;
            tutorResponseModel = restTemplate
                    .getForObject(url, TutorResponseModel.class);
            log.debug("5. Received in API-Gateway Lesson Service Client getLessonAggregate with lessonResponseModel: " + tutorResponseModel.getTutorId());
        }catch(HttpClientErrorException ex) {

            log.debug("5.");
            throw new RuntimeException(ex);
        }
        return tutorResponseModel;
    }


    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InadequateGpaException(getErrorMessage(ex));
        }
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }
}

