package com.tutoringhub.supervisorconfirmationservice.datalayer;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

public class SupervisorConfirmationIdentifier {

    @Indexed(unique = true)
    private String supervisorConfirmationId;

    public SupervisorConfirmationIdentifier() {
        this.supervisorConfirmationId = UUID.randomUUID().toString();
    }

    public SupervisorConfirmationIdentifier(String supervisorConfirmationId) {
        this.supervisorConfirmationId = supervisorConfirmationId;
    }


    public String getSupervisorConfirmationId() {
        return supervisorConfirmationId;
    }
}


