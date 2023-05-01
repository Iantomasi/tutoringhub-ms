package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;

public interface TutorService {

    TutorResponseModel getTutorAggregate(String tutorId);

}
