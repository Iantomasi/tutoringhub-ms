package com.tutoringhub.apigateway.businesslayer;


import com.tutoringhub.apigateway.domainclientlayer.SupervisorConfirmationServiceClient;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationRequestModel;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationResponseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SupervisorConfirmationServiceImpl implements SupervisorConfirmationService {


    private SupervisorConfirmationServiceClient supervisorConfirmationServiceClient;

    public SupervisorConfirmationServiceImpl(SupervisorConfirmationServiceClient supervisorConfirmationServiceClient) {
        this.supervisorConfirmationServiceClient = supervisorConfirmationServiceClient;
    }
//
//    @Override
//    public List<SupervisorConfirmationResponseModel> getAllSupervisorConfirmations() {
//
//        return supervisorConfirmationServiceClient.getAllSupervisorConfirmations();
//    }

    @Override
    public List<SupervisorConfirmationResponseModel> getStudentExtraCreditReport(String studentId) {
        return supervisorConfirmationServiceClient.getStudentExtraCreditReport(studentId);
    }

//    @Override
//    public SupervisorConfirmationResponseModel getSupervisorConfirmationById(String supervisorConfirmationId) {
//
//        return supervisorConfirmationServiceClient.getSupervisorConfirmationById(supervisorConfirmationId);
//    }

    @Override
    public SupervisorConfirmationResponseModel getSupervisorConfirmationByIdInStudentExtraCreditReport(String studentId, String supervisorConfirmationId) {
        return supervisorConfirmationServiceClient.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId, supervisorConfirmationId);
    }

    @Override
    public SupervisorConfirmationResponseModel processStudentExtraCreditSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId) {
        return supervisorConfirmationServiceClient.processStudentExtraCreditSupervisorConfirmation(supervisorConfirmationRequestModel, studentId);
    }

    @Override
    public SupervisorConfirmationResponseModel updateSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId, String supervisorConfirmationId) {
        return supervisorConfirmationServiceClient.updateSupervisorConfirmation(supervisorConfirmationRequestModel, studentId, supervisorConfirmationId);
    }

//    @Override
//    public void removeSupervisorConfirmation(String supervisorConfirmationId) {
//        supervisorConfirmationServiceClient.removeSupervisorConfirmation(supervisorConfirmationId);
//    }

    @Override
    public void removeStudentSupervisorConfirmation(String studentId, String supervisorConfirmationId) {
        supervisorConfirmationServiceClient.removeStudentSupervisorConfirmation(studentId, supervisorConfirmationId);
    }
}
