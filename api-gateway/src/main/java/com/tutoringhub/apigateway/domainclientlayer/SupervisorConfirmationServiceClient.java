package com.tutoringhub.apigateway.domainclientlayer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationRequestModel;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationResponseModel;
import com.tutoringhub.apigateway.utils.HttpErrorInfo;
import com.tutoringhub.apigateway.utils.exceptions.InsufficientCommentException;
import com.tutoringhub.apigateway.utils.exceptions.InvalidInputException;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
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
public class SupervisorConfirmationServiceClient {


    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private  final String SUPERVISORCONFIRMATION_BASE_URL;
    private  final String STUDENT_SUPERVISORCONFIRMATION_BASE_URL;

    public SupervisorConfirmationServiceClient(RestTemplate restTemplate,
                                 ObjectMapper objectMapper,
                                 @Value("${app.supervisorconfirmations-service.host}") String supervisorConfirmationServiceHost,
                                 @Value("${app.supervisorconfirmations-service.port}") String supervisorConfirmationServicePort) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;

        SUPERVISORCONFIRMATION_BASE_URL = "http://" + supervisorConfirmationServiceHost +":" + supervisorConfirmationServicePort + "/api/v1/supervisorconfirmations";
        STUDENT_SUPERVISORCONFIRMATION_BASE_URL = "http://" + supervisorConfirmationServiceHost +":" + supervisorConfirmationServicePort + "/api/v1/students";

    }


//    public List<SupervisorConfirmationResponseModel> getAllSupervisorConfirmations(){
//
//
//        try{
//            String url = SUPERVISORCONFIRMATION_BASE_URL;
//            SupervisorConfirmationResponseModel[] supervisorConfirmationsResponseArray =
//                    restTemplate.getForObject(url, SupervisorConfirmationResponseModel[].class);
//            return Arrays.asList(supervisorConfirmationsResponseArray);
//        }
//        catch (HttpClientErrorException ex){
//            throw handleHttpClientException(ex);
//        }
//    }

    public List<SupervisorConfirmationResponseModel> getStudentExtraCreditReport(String studentId) {

        try {
            String url = STUDENT_SUPERVISORCONFIRMATION_BASE_URL + "/" + studentId + "/supervisorconfirmations";

            SupervisorConfirmationResponseModel[] supervisorConfirmationsResponseArray =
                    restTemplate.getForObject(url, SupervisorConfirmationResponseModel[].class);

            return Arrays.asList(supervisorConfirmationsResponseArray);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

//    public SupervisorConfirmationResponseModel getSupervisorConfirmationById(String supervisorConfirmationId){
//
//        try {
//            String url = SUPERVISORCONFIRMATION_BASE_URL + "/" + supervisorConfirmationId;
//
//            SupervisorConfirmationResponseModel supervisorConfirmationResponseModel =
//                    restTemplate.getForObject(url, SupervisorConfirmationResponseModel.class);
//
//            return supervisorConfirmationResponseModel;
//        }
//        catch (HttpClientErrorException ex){
//            throw handleHttpClientException(ex);
//        }
//    }

    public SupervisorConfirmationResponseModel getSupervisorConfirmationByIdInStudentExtraCreditReport(String studentId, String supervisorConfirmationId){

        try{
            String url = STUDENT_SUPERVISORCONFIRMATION_BASE_URL + "/" + studentId + "/supervisorconfirmations" + "/" +  supervisorConfirmationId;

            SupervisorConfirmationResponseModel supervisorConfirmationResponseModel =
                    restTemplate.getForObject(url, SupervisorConfirmationResponseModel.class);
            return supervisorConfirmationResponseModel;
        }
        catch (HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }


    public SupervisorConfirmationResponseModel processStudentExtraCreditSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId){
        try{
            String url = STUDENT_SUPERVISORCONFIRMATION_BASE_URL + "/" + studentId + "/supervisorconfirmations";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SupervisorConfirmationRequestModel> requestEntity = new HttpEntity<>(supervisorConfirmationRequestModel, headers);

            SupervisorConfirmationResponseModel supervisorConfirmationResponseModel =
                    restTemplate.postForObject(url, requestEntity, SupervisorConfirmationResponseModel.class);

            return supervisorConfirmationResponseModel;
        }

        catch(HttpClientErrorException ex){
            log.debug("5. Received in API-Gateway PurchaseServiceClient addPurchase with exception: " + ex.getMessage());
            throw handleHttpClientException(ex);
        }
    }

    public SupervisorConfirmationResponseModel updateSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId, String supervisorConfirmationId){

        try{
            String url = STUDENT_SUPERVISORCONFIRMATION_BASE_URL + "/" + studentId + "/supervisorconfirmations" + "/" + supervisorConfirmationId;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<SupervisorConfirmationRequestModel> requestEntity = new HttpEntity<>(supervisorConfirmationRequestModel, headers);

            restTemplate.put(url, requestEntity, studentId, supervisorConfirmationId);

            SupervisorConfirmationResponseModel supervisorConfirmationResponseModel = restTemplate
                    .getForObject(url, SupervisorConfirmationResponseModel.class);

            return supervisorConfirmationResponseModel;
        }

        catch(HttpClientErrorException ex){
            throw handleHttpClientException(ex);
        }
    }

//    public void removeSupervisorConfirmation(String supervisorConfirmationId) {
//
//        try {
//            String url = SUPERVISORCONFIRMATION_BASE_URL + "/" + supervisorConfirmationId;
//
//            restTemplate.delete(url);
//
//        } catch (HttpClientErrorException ex) {
//            throw handleHttpClientException(ex);
//        }
//    }

    public void removeStudentSupervisorConfirmation(String studentId, String supervisorConfirmationId) {

        try {
            String url = STUDENT_SUPERVISORCONFIRMATION_BASE_URL + "/" + studentId + "/supervisorconfirmations" + "/" + supervisorConfirmationId;
            restTemplate.delete(url);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try {
            return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch (IOException ioex) {
            return ioex.getMessage();
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        //include all possible responses from the client

        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(getErrorMessage(ex));
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
            return new InvalidInputException(ex);
        }
        if (ex.getStatusCode() == UNPROCESSABLE_ENTITY) {
        return new InsufficientCommentException(getErrorMessage(ex));
        }
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}