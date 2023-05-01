package com.tutoringhub.apigateway.businesslayer;

import com.tutoringhub.apigateway.domainclientlayer.TutorServiceClient;
import com.tutoringhub.apigateway.presentationlayer.tutor.TutorResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TutorServiceImpl implements TutorService {

    private TutorServiceClient tutorServiceClient;

    public TutorServiceImpl(TutorServiceClient tutorServiceClient) {
        this.tutorServiceClient = tutorServiceClient;
    }

    @Override
    public TutorResponseModel getTutorAggregate(String tutorId) {
        return tutorServiceClient.getTutorAggregate(tutorId);
    }
}
