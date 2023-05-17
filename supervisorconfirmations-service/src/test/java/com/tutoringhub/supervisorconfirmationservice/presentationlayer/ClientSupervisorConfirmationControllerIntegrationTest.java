package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class ClientSupervisorConfirmationControllerIntegrationTest {

}