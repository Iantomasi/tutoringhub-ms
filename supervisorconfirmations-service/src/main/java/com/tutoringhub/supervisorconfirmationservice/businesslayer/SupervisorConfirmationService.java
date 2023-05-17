package com.tutoringhub.supervisorconfirmationservice.businesslayer;

import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationRequestModel;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;

import java.util.List;
import java.util.Map;

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

