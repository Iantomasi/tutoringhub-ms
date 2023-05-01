package com.tutoringhub.apigateway.presentationlayer.tutor;

import com.tutoringhub.apigateway.businesslayer.TutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/tutors")
public class TutorController {

    private TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }


    ResponseEntity<TutorResponseModel> getTutorAggregate(@PathVariable String tutorId){
        log.debug("1. Received in Api-Gateway tutor controller getTutorAggregate with tutorId: " + tutorId);
        return ResponseEntity.ok().body(tutorService.getTutorAggregate(tutorId));
    }
}
