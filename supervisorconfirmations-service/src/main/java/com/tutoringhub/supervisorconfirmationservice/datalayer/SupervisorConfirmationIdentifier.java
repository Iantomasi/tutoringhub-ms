package com.tutoringhub.supervisorconfirmationservice.datalayer;

import org.springframework.data.mongodb.core.index.Indexed;

import java.util.UUID;

public class SupervisorConfirmationIdentifier {

    @Indexed(unique = true)
    private String supervisorConfirmationId;

    public SupervisorConfirmationIdentifier() {
        this.supervisorConfirmationId = UUID.randomUUID().toString();
    }

    public String getSupervisorConfirmationId() {
        return supervisorConfirmationId;
    }
}


