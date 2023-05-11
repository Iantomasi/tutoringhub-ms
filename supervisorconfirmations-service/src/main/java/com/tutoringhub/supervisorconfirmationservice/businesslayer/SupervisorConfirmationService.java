package com.tutoringhub.supervisorconfirmationservice.businesslayer;

import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationRequestModel;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;

import java.util.List;

public interface SupervisorConfirmationService {


    List<SupervisorConfirmationResponseModel> getAllSupervisorConfirmations();
    SupervisorConfirmationResponseModel processStudentExtraCreditSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId);


}
