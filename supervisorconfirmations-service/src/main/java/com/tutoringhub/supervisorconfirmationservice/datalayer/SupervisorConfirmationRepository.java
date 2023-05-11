package com.tutoringhub.supervisorconfirmationservice.datalayer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SupervisorConfirmationRepository extends MongoRepository<SupervisorConfirmation, String> {

}
