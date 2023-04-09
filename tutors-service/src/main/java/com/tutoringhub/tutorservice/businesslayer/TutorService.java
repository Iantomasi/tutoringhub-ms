package com.tutoringhub.tutorservice.businesslayer;


import com.tutoringhub.tutorservice.presentationlayer.TutorRequestModel;
import com.tutoringhub.tutorservice.presentationlayer.TutorResponseModel;

import java.util.List;

public interface TutorService {

    List<TutorResponseModel> getTutors();
    TutorResponseModel getTutorByTutorId(String tutorId);
    TutorResponseModel addTutor(TutorRequestModel tutorRequestModel);
    TutorResponseModel updateTutor(TutorRequestModel tutorRequestModel, String tutorId);
    void removeTutor(String tutorId);
}
