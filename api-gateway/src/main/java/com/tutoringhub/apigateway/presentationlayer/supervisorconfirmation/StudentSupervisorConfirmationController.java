package com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation;

import com.tutoringhub.apigateway.businesslayer.SupervisorConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/students/{studentId}/supervisorconfirmations")
public class StudentSupervisorConfirmationController {

    private SupervisorConfirmationService supervisorConfirmationService;

    public StudentSupervisorConfirmationController(SupervisorConfirmationService supervisorConfirmationService) {
        this.supervisorConfirmationService = supervisorConfirmationService;
    }

    @GetMapping(produces ="application/json")
    ResponseEntity<List<SupervisorConfirmationResponseModel>> getStudentExtraCreditReport(@PathVariable String studentId){
        return ResponseEntity.ok().body(supervisorConfirmationService.getStudentExtraCreditReport(studentId));
    }

    @GetMapping(value ="/{supervisorConfirmationId}" ,produces ="application/json")
    ResponseEntity<SupervisorConfirmationResponseModel> getSupervisorConfirmationByIdInStudentExtraCreditReport(@PathVariable String studentId, @PathVariable String supervisorConfirmationId){
        return ResponseEntity.ok().body(supervisorConfirmationService.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId, supervisorConfirmationId));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    ResponseEntity<SupervisorConfirmationResponseModel> processStudentExtraCreditSupervisorConfirmation(@RequestBody SupervisorConfirmationRequestModel supervisorConfirmationRequestModel,
                                                                  @PathVariable String studentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supervisorConfirmationService.processStudentExtraCreditSupervisorConfirmation(supervisorConfirmationRequestModel, studentId));
    }

    @PutMapping(value = "/{supervisorConfirmationId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<Void> updateSupervisorConfirmation(@RequestBody SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, @PathVariable String studentId, @PathVariable String supervisorConfirmationId) {
        supervisorConfirmationService.updateSupervisorConfirmation(supervisorConfirmationRequestModel, studentId, supervisorConfirmationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{supervisorConfirmationId}", produces = "application/json")
    ResponseEntity<Void> removeStudentSupervisorConfirmation(@PathVariable String studentId, @PathVariable String supervisorConfirmationId) {
        supervisorConfirmationService.removeStudentSupervisorConfirmation(studentId, supervisorConfirmationId);
        return ResponseEntity.noContent().build();
    }

}

