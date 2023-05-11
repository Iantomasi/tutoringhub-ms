package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.InvalidInputException;
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.NotFoundException;
import com.tutoringhub.supervisorconfirmationservice.utils.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
                              @Value("${app.tutors-service.port}") String tutorsServicePort){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.TUTOR_SERVICE_BASE_URL = "http://" + tutorsServiceHost + ":" + tutorsServicePort + "/api/v1/tutors";
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
            throw handleHttpClientException(ex);
        }
        return tutorResponseModel;
    }


    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(getErrorMessage(ex));
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

