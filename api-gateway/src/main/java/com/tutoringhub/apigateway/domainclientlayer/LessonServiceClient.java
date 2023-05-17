package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonRequestModel;
import com.tutoringhub.apigateway.presentationlayer.lesson.LessonResponseModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorRequestModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import com.tutoringhub.apigateway.utils.exceptions.UnregisteredLessonSubjectException;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RequestCallback;
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

    public List<LessonResponseModel> getAllLessonsAggregate(){
        //log.debug("3. Received in Api-Gateway Lesson Service Client getAllLessonsAggregate");
        try{
            String url= LESSON_SERVICE_BASE_URL;

            LessonResponseModel[] lessonResponseArray = restTemplate
                    .getForObject(url, LessonResponseModel[].class);
            return Arrays.asList(lessonResponseArray);

        }catch (HttpClientErrorException ex){
            // log.debug("5. Received in Api-Gateway Lesson Service Client getAllLessonsAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public LessonResponseModel getLessonAggregate(String lessonId){

        try {
            String url = LESSON_SERVICE_BASE_URL + "/" + lessonId;
            LessonResponseModel lessonResponseModel = restTemplate
                    .getForObject(url, LessonResponseModel.class);
            // log.debug("5. Received in API-Gateway Lesson Service Client getLessonAggregate with lessonResponseModel: " + lessonResponseModel.getLessonId());
            return lessonResponseModel;
        }
        catch(HttpClientErrorException ex) {
            throw  handleHttpClientException(ex);
        }
    }


    public LessonResponseModel addLessonAggregate(LessonRequestModel lessonRequestModel) {

       // log.debug("3. Received in Api-Gateway Lesson Service Client addLessonAggregate");

        try {

            String url = LESSON_SERVICE_BASE_URL;
            LessonResponseModel lessonResponseModel = restTemplate.postForObject(url, lessonRequestModel, LessonResponseModel.class);
            return lessonResponseModel;

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public LessonResponseModel updateLessonAggregate(LessonRequestModel lessonRequestModel, String lessonId) {

       // log.debug("3. Received in Api-Gateway Lesson Service Client updateLessonAggregate with lessonId: " + lessonId);

        try {

            String url = LESSON_SERVICE_BASE_URL + "/" + lessonId;

            restTemplate.put(url, lessonRequestModel, lessonId);

            LessonResponseModel lessonResponseModel = restTemplate
                    .getForObject(url, LessonResponseModel.class);
            //log.debug("5. Received in Api-Gateway Lesson Service Client updateLessonAggregate with lessonResponseModel: " + lessonResponseModel.getLessonId());
            return lessonResponseModel;
        } catch (HttpClientErrorException ex) {
          //  log.debug("5. Received in Api-Gateway Lesson Service Client updateLessonAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public void removeLessonAggregate(String lessonId){

        //log.debug("3. Received in Api-Gateway Lesson Service Client removeLessonAggregate with lessonId: " + lessonId);
        try{
            String url = LESSON_SERVICE_BASE_URL + "/" + lessonId;
             restTemplate.delete(url);
             //  log.debug("5. Received in Api-Gateway Lesson Service Client removeLessonAggregate with lessonId: " + lessonId);
        }catch (HttpClientErrorException ex){
           // log.debug("5. Received in Api-Gateway Lesson Service Client removeLessonAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new UnregisteredLessonSubjectException(getErrorMessage(ex));
        }
      //  log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
       // log.warn("Error body: {}", ex.getResponseBodyAsString());
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