package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson;

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
public class LessonServiceClient {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String LESSON_SERVICE_BASE_URL;

    public LessonServiceClient(RestTemplate restTemplate,
                               ObjectMapper objectMapper,
                               @Value("${app.lessons-service.host}") String lessonsServiceHost,
                               @Value("${app.lessons-service.port}") String lessonsServicePort){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.LESSON_SERVICE_BASE_URL = "http://" + lessonsServiceHost + ":" + lessonsServicePort + "/api/v1/lessons";
    }


    public LessonResponseModel getLessonAggregate(String lessonId){

        LessonResponseModel lessonResponseModel;
        try {
            String url = LESSON_SERVICE_BASE_URL + "/" + lessonId;
            lessonResponseModel = restTemplate
                    .getForObject(url, LessonResponseModel.class);
            log.debug("5. Received in API-Gateway Lesson Service Client getLessonAggregate with lessonResponseModel: " + lessonResponseModel.getLessonId());
        }catch(HttpClientErrorException ex) {
            log.debug("5.");
            throw  handleHttpClientException(ex);
        }
         return lessonResponseModel;
    }



    public void updateLessonStatus(LessonRequestModel lessonRequestModel, String lessonId){
        log.debug("3. Received in API-Gateway Lesson Service Client updateLessonStatus");

        try{

          String url= LESSON_SERVICE_BASE_URL+"/"+lessonId;
          restTemplate.put(url, lessonRequestModel);


            log.debug("5. Received in API-Gateway Lesson Service Client updateLessonStatus with lessonId: "
                    + lessonId);

        }catch (HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway Lesson Service Client updateLessonStatus with exception: "
                    +ex.getMessage());
            throw handleHttpClientException(ex);

        }

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