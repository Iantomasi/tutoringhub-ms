package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import com.tutoringhub.supervisorconfirmationservice.businesslayer.SupervisorConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}