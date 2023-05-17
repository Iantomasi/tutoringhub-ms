package com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation;

import com.tutoringhub.apigateway.businesslayer.SupervisorConfirmationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/supervisorconfirmations")
public class SupervisorConfirmationController {

    private SupervisorConfirmationService supervisorConfirmationService;

    public SupervisorConfirmationController(SupervisorConfirmationService supervisorConfirmationService) {
        this.supervisorConfirmationService = supervisorConfirmationService;
    }

//    @GetMapping(produces ="application/json")
//    ResponseEntity<List<SupervisorConfirmationResponseModel>> getAllSupervisorConfirmations(){
//        return ResponseEntity.ok().body(supervisorConfirmationService.getAllSupervisorConfirmations());
//    }

//    @GetMapping(value ="/{supervisorConfirmationId}" ,produces ="application/json")
//    ResponseEntity<SupervisorConfirmationResponseModel> getSupervisorConfirmationById(@PathVariable String supervisorConfirmationId){
//        return ResponseEntity.ok().body(supervisorConfirmationService.getSupervisorConfirmationById(supervisorConfirmationId));
//    }


//    @DeleteMapping(value = "/{supervisorConfirmationId}", produces = "application/json")
//    ResponseEntity<Void> removeSupervisorConfirmation(@PathVariable String supervisorConfirmationId) {
//        supervisorConfirmationService.removeSupervisorConfirmation(supervisorConfirmationId);
//        return ResponseEntity.noContent().build();
//    }

}
