package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.presentationlayer.tutor.TutorRequestModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;

import java.util.List;

public interface TutorService {

    List<TutorResponseModel> getAllTutorsAggregate();
    TutorResponseModel getTutorAggregate(String tutorId);
    TutorResponseModel addTutorAggregate(TutorRequestModel tutorRequestModel);
    TutorResponseModel updateTutorAggregate(TutorRequestModel tutorRequestModel, String tutorId);
    void removeTutorAggregate(String tutorId);

}
