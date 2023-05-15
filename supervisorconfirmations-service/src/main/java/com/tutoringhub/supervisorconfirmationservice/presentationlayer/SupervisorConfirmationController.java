package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import com.tutoringhub.supervisorconfirmationservice.businesslayer.SupervisorConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/supervisorconfirmations")
@RequiredArgsConstructor
public class SupervisorConfirmationController {

    private final SupervisorConfirmationService supervisorConfirmationService;

    @GetMapping
    ResponseEntity<List<SupervisorConfirmationResponseModel>> getAllSupervisorConfirmations() {
        return ResponseEntity.ok().body(supervisorConfirmationService.getAllSupervisorConfirmations());
    }


    @GetMapping("/{supervisorConfirmationId}")
    ResponseEntity<SupervisorConfirmationResponseModel> getSupervisorConfirmationById(@PathVariable String supervisorConfirmationId) {
        return ResponseEntity.ok().body(supervisorConfirmationService.getSupervisorConfirmationById(supervisorConfirmationId));
    }


    @DeleteMapping("/{supervisorConfirmationId}")
    ResponseEntity<Void> removeSupervisorConfirmation(@PathVariable String supervisorConfirmationId) {
        supervisorConfirmationService.removeSupervisorConfirmation(supervisorConfirmationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}