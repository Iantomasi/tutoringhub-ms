package com.tutoringhub.apigateway.businesslayer;


import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationRequestModel;
import com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation.SupervisorConfirmationResponseModel;

import java.util.List;

public interface SupervisorConfirmationService {


   //List<SupervisorConfirmationResponseModel> getAllSupervisorConfirmations();
    List<SupervisorConfirmationResponseModel> getStudentExtraCreditReport(String studentId);
    //SupervisorConfirmationResponseModel getSupervisorConfirmationById(String supervisorConfirmationId);
    SupervisorConfirmationResponseModel getSupervisorConfirmationByIdInStudentExtraCreditReport(String studentId, String supervisorConfirmationId);
    SupervisorConfirmationResponseModel processStudentExtraCreditSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId);
    SupervisorConfirmationResponseModel updateSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId, String supervisorConfirmationId);
    //void removeSupervisorConfirmation(String supervisorConfirmationId);
    void removeStudentSupervisorConfirmation(String studentId, String supervisorConfirmationId);
}

