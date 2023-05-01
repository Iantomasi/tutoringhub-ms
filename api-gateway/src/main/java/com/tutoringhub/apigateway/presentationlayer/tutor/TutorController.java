package com.tutoringhub.apigateway.presentationlayer.tutor;

import com.tutoringhub.apigateway.businesslayer.TutorService;
import com.tutoringhub.apigateway.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/tutors")
public class TutorController {

    private final Integer UUID_SIZE = 36;
    private TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<TutorResponseModel>> getAllTutorsAggregate(){
        log.debug("1. Received in API-Gateway Tutor Controller getAllTutorsAggregate");
        return ResponseEntity.ok().body(tutorService.getAllTutorsAggregate());
    }

    @GetMapping(
            value = "/{tutorId}",
            produces = "application/json"
    )
    ResponseEntity<TutorResponseModel> getTutorAggregate(@PathVariable String tutorId){
        if(tutorId.length() != UUID_SIZE){
            throw new NotFoundException("No tutor assigned to this tutorId");
        }

        log.debug("1. Received in Api-Gateway Tutor Controller getTutorAggregate with tutorId: " + tutorId);
        return ResponseEntity.ok().body(tutorService.getTutorAggregate(tutorId));
    }

    @PostMapping()
    ResponseEntity<TutorResponseModel> addTutorAggregate(@RequestBody TutorRequestModel tutorRequestModel){

        log.debug("1. Received in Api-Gateway Tutor Controller addTutorAggregate");
        return ResponseEntity.status(HttpStatus.CREATED).body(tutorService.addTutorAggregate(tutorRequestModel));
    }

    @PutMapping(
            value = "/{tutorId}",
            produces = "application/json"
    )
    ResponseEntity<TutorResponseModel> updateTutorAggregate(@RequestBody TutorRequestModel tutorRequestModel, @PathVariable String tutorId){
        if(tutorId.length() != UUID_SIZE){
            throw new NotFoundException("No tutor assigned to this tutorId");
        }
        log.debug("1. Received in Api-Gateway Tutor Controller updateTutorAggregate with tutorId: " + tutorId);
        return ResponseEntity.ok().body(tutorService.updateTutorAggregate(tutorRequestModel, tutorId));
    }


    @DeleteMapping(
            value = "/{tutorId}",
            produces = "application/json"
    )
    ResponseEntity<Void> removeTutorAggregate(@PathVariable String tutorId){
        if(tutorId.length() != UUID_SIZE){
            throw new NotFoundException("No tutor assigned to this tutorId");
        }
        log.debug("1. Received in Api-Gateway Tutor Controller removeTutorAggregate with tutorId: " + tutorId);

        tutorService.removeTutorAggregate(tutorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
