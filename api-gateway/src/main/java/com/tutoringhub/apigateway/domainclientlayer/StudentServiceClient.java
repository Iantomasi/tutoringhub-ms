package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tutoringhub.apigateway.presentationlayer.student.StudentRequestModel;
import com.tutoringhub.apigateway.presentationlayer.student.StudentResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.DuplicateEmailException;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class StudentServiceClient {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String STUDENT_SERVICE_BASE_URL;

    public StudentServiceClient(RestTemplate restTemplate,
                               ObjectMapper objectMapper,
                               @Value("${app.students-service.host}") String studentsServiceHost,
                               @Value("${app.students-service.port}") String studentsServicePort){
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.STUDENT_SERVICE_BASE_URL = "http://" + studentsServiceHost + ":" + studentsServicePort + "/api/v1/students";
    }


    public List<StudentResponseModel> getAllStudentsAggregate(){
       // log.debug("3. Received in Api-Gateway Student Service Client getAllStudentsAggregate");
        try{
            String url= STUDENT_SERVICE_BASE_URL;

            StudentResponseModel[] studentResponseArray = restTemplate
                    .getForObject(url, StudentResponseModel[].class);
            return Arrays.asList(studentResponseArray);

        }catch (HttpClientErrorException ex){
           // log.debug("5. Received in Api-Gateway Student Service Client getAllStudentsAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public StudentResponseModel getStudentAggregate(String studentId){

        StudentResponseModel studentResponseModel;
        try {
            String url = STUDENT_SERVICE_BASE_URL + "/" + studentId;
            studentResponseModel = restTemplate
                    .getForObject(url, StudentResponseModel.class);
          //  log.debug("5. Received in API-Gateway Student Service Client getStudentAggregate with studentResponseModel: " + studentResponseModel.getStudentId());
        }catch(HttpClientErrorException ex) {
      //      log.debug("5.");
            throw handleHttpClientException(ex);
        }
        return studentResponseModel;
    }


    public StudentResponseModel addStudentAggregate(StudentRequestModel studentRequestModel) {

     //   log.debug("3. Received in Api-Gateway Student Service Client addStudentAggregate");

        try {

            String url = STUDENT_SERVICE_BASE_URL;

            StudentResponseModel studentResponseModel = restTemplate.postForObject(url, studentRequestModel, StudentResponseModel.class);
            return studentResponseModel;
        } catch (HttpClientErrorException ex) {

        //    log.debug("5. Received in Api-Gateway Student Service Client addStudentAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public StudentResponseModel updateStudentAggregate(StudentRequestModel studentRequestModel, String studentId) {

    //    log.debug("3. Received in Api-Gateway Student Service Client updateStudentAggregate with studentId: " + studentId);
        try {
            String url = STUDENT_SERVICE_BASE_URL + "/" + studentId;

            restTemplate.put(url, studentRequestModel, studentId);
            StudentResponseModel studentResponseModel = restTemplate
                    .getForObject(url, StudentResponseModel.class);
        //    log.debug("5. Received in Api-Gateway Student Service Client updateStudentAggregate with studentResponseModel: " + studentResponseModel.getStudentId());
            return studentResponseModel;
        } catch (HttpClientErrorException ex) {
        //    log.debug("5. Received in Api-Gateway Student Service Client updateStudentAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public void removeStudentAggregate(String studentId){

    //    log.debug("3. Received in Api-Gateway Student Service Client removeStudentAggregate with studentId: " + studentId);

        try{

            String url = STUDENT_SERVICE_BASE_URL + "/" + studentId;
            restTemplate.delete(url);
            //   log.debug("5. Received in Api-Gateway Student Service Client removeStudentAggregate with studentId: " + studentId);
        }catch (HttpClientErrorException ex){
           // log.debug("5. Received in Api-Gateway Student Service Client removeStudentAggregate with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new DuplicateEmailException(getErrorMessage(ex));
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


