package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.domainclientlayer.TutorServiceClient;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorRequestModel;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TutorServiceImpl implements TutorService {

    private TutorServiceClient tutorServiceClient;

    public TutorServiceImpl(TutorServiceClient tutorServiceClient) {
        this.tutorServiceClient = tutorServiceClient;
    }

    @Override
    public List<TutorResponseModel> getAllTutorsAggregate() {
        // log.debug("2. Received in Api-Gateway Tutor Service Impl getAllTutorsAggregate");
        return tutorServiceClient.getAllTutorsAggregate();
    }

    @Override
    public TutorResponseModel getTutorAggregate(String tutorId) {
        // log.debug("2. Received in Api-Gateway Tutor Service Impl getTutorAggregate with tutorId: " + tutorId);
        return tutorServiceClient.getTutorAggregate(tutorId);
    }

    @Override
    public TutorResponseModel addTutorAggregate(TutorRequestModel tutorRequestModel) {
       // log.debug("2. Received in Api-Gateway Tutor Service Impl addTutorAggregate");
        return tutorServiceClient.addTutorAggregate(tutorRequestModel);
    }

    @Override
    public TutorResponseModel updateTutorAggregate(TutorRequestModel tutorRequestModel, String tutorId) {
       // log.debug("2. Received in Api-Gateway Tutor Service Impl updateTutorAggregate with tutorId" + tutorId);
        return tutorServiceClient.updateTutorAggregate(tutorRequestModel, tutorId);
    }

    @Override
    public void removeTutorAggregate(String tutorId) {
        // log.debug("2. Received in Api-Gateway Tutor Service Impl removeTutorAggregate with tutorId" + tutorId);
        tutorServiceClient.removeTutorAggregate(tutorId);
    }
}
