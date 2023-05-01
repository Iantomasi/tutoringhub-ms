package com.tutoringhub.tutorservice.presentationlayer;

import com.tutoringhub.tutorservice.businesslayer.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tutors")
public class TutorController {

    private TutorService tutorService;

    public TutorController(TutorService tutorService){ this.tutorService = tutorService; }

    @GetMapping()
    public List<TutorResponseModel> getTutors(){ return tutorService.getTutors(); }

    @GetMapping("/{tutorId}")
    public TutorResponseModel getTutorByTutorId(@PathVariable String tutorId){
        return tutorService.getTutorByTutorId(tutorId);
    }

    @PostMapping()
    ResponseEntity<TutorResponseModel> addTutor(@RequestBody TutorRequestModel tutorRequestModel){
        return ResponseEntity.status(HttpStatus.CREATED).body(tutorService.addTutor(tutorRequestModel));
    }


    @PutMapping("/{tutorId}")
    ResponseEntity<TutorResponseModel> updateTutor(@RequestBody TutorRequestModel tutorRequestModel, @PathVariable String tutorId){
        return ResponseEntity.ok().body(tutorService.updateTutor(tutorRequestModel, tutorId));
    }

    @DeleteMapping("/{tutorId}")
     ResponseEntity<Void> removeTutor(@PathVariable String tutorId){
        tutorService.removeTutor(tutorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
