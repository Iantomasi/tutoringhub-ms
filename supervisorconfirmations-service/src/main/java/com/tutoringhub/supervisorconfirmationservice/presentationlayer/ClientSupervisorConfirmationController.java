package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import com.tutoringhub.supervisorconfirmationservice.businesslayer.SupervisorConfirmationService;
import com.tutoringhub.supervisorconfirmationservice.datalayer.SupervisorConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/students/{studentId}/supervisorconfirmations")
@RequiredArgsConstructor
public class ClientSupervisorConfirmationController {

    private final SupervisorConfirmationService supervisorConfirmationService;


    @PostMapping()
    ResponseEntity<SupervisorConfirmationResponseModel>processClientSupervisorConfirmation(
            @RequestBody SupervisorConfirmationRequestModel supervisorConfirmationRequestModel,
            @PathVariable String studentId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(supervisorConfirmationService.processStudentExtraCreditSupervisorConfirmation(supervisorConfirmationRequestModel, studentId));
    }
}
