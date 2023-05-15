package com.tutoringhub.supervisorconfirmationservice.datalayer;

import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SupervisorConfirmationRepository extends MongoRepository<SupervisorConfirmation, String> {

    SupervisorConfirmation findSupervisorConfirmationBySupervisorConfirmationIdentifier_supervisorConfirmationId(String supervisorConfirmationId);
    List<SupervisorConfirmation> findAllSupervisorConfirmationsByStudentIdentifier_studentId(String studentId);
}
