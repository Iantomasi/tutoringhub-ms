package com.tutoringhub.tutorservice.presentationlayer;

import com.tutoringhub.tutorservice.businesslayer.TutorService;
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
    public TutorResponseModel addTutor(@RequestBody TutorRequestModel tutorRequestModel){
        return tutorService.addTutor(tutorRequestModel);
    }


    @PutMapping("/{tutorId}")
    public TutorResponseModel updateTutor(@RequestBody TutorRequestModel tutorRequestModel, @PathVariable String tutorId){
        return tutorService.updateTutor(tutorRequestModel, tutorId);
    }

    @DeleteMapping("/{tutorId}")
    public void removeTutor(@PathVariable String tutorId){
        tutorService.removeTutor(tutorId);
    }
}
