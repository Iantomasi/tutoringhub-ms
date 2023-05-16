package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import com.tutoringhub.supervisorconfirmationservice.businesslayer.SupervisorConfirmationService;
import com.tutoringhub.supervisorconfirmationservice.datalayer.SupervisorConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/v1/students/{studentId}/supervisorconfirmations")
@RequiredArgsConstructor
public class ClientSupervisorConfirmationController {

    private final SupervisorConfirmationService supervisorConfirmationService;


    @GetMapping
    ResponseEntity<List<SupervisorConfirmationResponseModel>> getStudentExtraCreditReport(@PathVariable String studentId) {
        return ResponseEntity.ok().body(supervisorConfirmationService.getStudentExtraCreditReport(studentId));
    }

    @GetMapping("/{supervisorConfirmationId}")
    ResponseEntity<SupervisorConfirmationResponseModel> getSupervisorConfirmationInStudentExtraCreditReport(@PathVariable String studentId, @PathVariable String supervisorConfirmationId){
        return ResponseEntity.ok().body(supervisorConfirmationService.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId, supervisorConfirmationId));
    }

    @PostMapping()
    ResponseEntity<SupervisorConfirmationResponseModel>processClientSupervisorConfirmation(
            @RequestBody SupervisorConfirmationRequestModel supervisorConfirmationRequestModel,
            @PathVariable String studentId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(supervisorConfirmationService.processStudentExtraCreditSupervisorConfirmation(supervisorConfirmationRequestModel, studentId));
    }

    @PutMapping("/{supervisorConfirmationId}")
    ResponseEntity<SupervisorConfirmationResponseModel> updateClientSupervisorConfirmation(
            @RequestBody SupervisorConfirmationRequestModel supervisorConfirmationRequestModel,
            @PathVariable String studentId,
            @PathVariable String supervisorConfirmationId) {

        SupervisorConfirmationResponseModel updatedSupervisorConfirmation = supervisorConfirmationService.updateSupervisorConfirmation(supervisorConfirmationRequestModel, studentId, supervisorConfirmationId);

        return ResponseEntity.ok(updatedSupervisorConfirmation);
    }

    @DeleteMapping("/{supervisorConfirmationId}")
    ResponseEntity<Void> removeSupervisorConfirmationInStudentExtraCreditReport(@PathVariable String studentId, @PathVariable String supervisorConfirmationId){
        supervisorConfirmationService.removeStudentSupervisorConfirmation(studentId, supervisorConfirmationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

