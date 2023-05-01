package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorRequestModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.InadequateGpaException;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import com.tutoringhub.apigateway.utils.exceptions.UnregisteredLessonSubjectException;
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


    public List<TutorResponseModel> getAllTutorsAggregate(){
        log.debug("3. Received in Api-Gateway Tutor Service Client getAllTutorsAggregate");
        try{
            String url= TUTOR_SERVICE_BASE_URL;

            TutorResponseModel[] tutorsResponseArray = restTemplate
                    .getForObject(url, TutorResponseModel[].class);
            return Arrays.asList(tutorsResponseArray);
        }catch (HttpClientErrorException ex){
            log.debug("5. Received in Api-Gateway Tutor Service Client getAllTutorsAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
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


    public TutorResponseModel addTutorAggregate(TutorRequestModel tutorRequestModel) {

        log.debug("3. Received in Api-Gateway Tutor Service Client addTutorAggregate");

        try {

            String url = TUTOR_SERVICE_BASE_URL;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TutorRequestModel> requestModelHttpEntity = new HttpEntity<>(tutorRequestModel, headers);

            TutorResponseModel tutorResponseModel = restTemplate.postForObject(url, requestModelHttpEntity, TutorResponseModel.class);
            return tutorResponseModel;
        } catch (HttpClientErrorException ex) {

            log.debug("5. Received in Api-Gateway Tutor Service Client addTutorAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

        public TutorResponseModel updateTutorAggregate(TutorRequestModel tutorRequestModel, String tutorId) {

            log.debug("3. Received in Api-Gateway Tutor Service Client updateTutorAggregate with tutorId: " + tutorId);


            try {

                String url = TUTOR_SERVICE_BASE_URL + "/" + tutorId;

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<TutorRequestModel> requestModelHttpEntity = new HttpEntity<>(tutorRequestModel, headers);

                restTemplate.put(url, requestModelHttpEntity, tutorId);

                TutorResponseModel tutorResponseModel = restTemplate
                        .getForObject(url, TutorResponseModel.class);
                log.debug("5. Received in Api-Gateway Tutor Service Client updateTutorAggregate with tutorResponseModel: " + tutorResponseModel.getTutorId());
                return tutorResponseModel;
            } catch (HttpClientErrorException ex) {
                log.debug("5. Received in Api-Gateway Tutor Service Client updateTutorAggregate with exception: " + ex.getMessage());
                throw handleHttpClientException(ex);
            }
        }

        public void removeTutorAggregate(String tutorId){

        log.debug("3. Received in Api-Gateway Tutor Service Client removeTutorAggregate with tutorId: " + tutorId);

        try{

            String url = TUTOR_SERVICE_BASE_URL + "/" + tutorId;
            restTemplate.delete(url);
            log.debug("5. Received in Api-Gateway Tutor Service Client removeTutorAggregate with tutorId: " + tutorId);
        }catch (HttpClientErrorException ex){

            log.debug("5. Received in Api-Gateway Tutor Service Client removeTutorAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
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

