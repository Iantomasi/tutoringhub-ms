package com.tutoringhub.supervisorconfirmationservice.businesslayer;

import com.tutoringhub.supervisorconfirmationservice.datalayer.*;
import com.tutoringhub.supervisorconfirmationservice.datamapperlayer.SupervisorConfirmationResponseModelMapper;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonRequestModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonResponseModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonServiceClient;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonStatus;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.student.StudentResponseModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.student.StudentServiceClient;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor.TutorResponseModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor.TutorServiceClient;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationRequestModel;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupervisorConfirmationServiceImpl implements SupervisorConfirmationService {

    private final SupervisorConfirmationRepository supervisorConfirmationRepository;
    private final SupervisorConfirmationResponseModelMapper supervisorConfirmationResponseModelMapper;
    private final LessonServiceClient lessonServiceClient;
    private final StudentServiceClient studentServiceClient;
    private final TutorServiceClient tutorServiceClient;


    @Override
    public List<SupervisorConfirmationResponseModel> getAllSupervisorConfirmations() {

        List<SupervisorConfirmation> supervisorConfirmations = supervisorConfirmationRepository.findAll();
        return supervisorConfirmationResponseModelMapper.entityListToResponseModelList(supervisorConfirmations);
    }

    @Override
    public SupervisorConfirmationResponseModel processStudentExtraCreditSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId) {


        StudentResponseModel studentResponseModel = studentServiceClient.getStudentAggregate(studentId);
        if(studentResponseModel == null){
            throw new NotFoundException("Unknown studentId provided: " + studentId);
        }

        LessonResponseModel lessonResponseModel = lessonServiceClient.getLessonAggregate(supervisorConfirmationRequestModel.getLessonId());
        if(lessonResponseModel == null){
            throw new NotFoundException("Unknown lessonId provided: " + supervisorConfirmationRequestModel.getLessonId());
        }

        TutorResponseModel tutorResponseModel = tutorServiceClient.getTutorAggregate(supervisorConfirmationRequestModel.getTutorId());
        if(tutorResponseModel == null){
            throw new NotFoundException("Unknown tutorId provided: " + supervisorConfirmationRequestModel.getTutorId());
        }

        SupervisorConfirmation supervisorConfirmation = SupervisorConfirmation.builder()
                .supervisorConfirmationIdentifier(new SupervisorConfirmationIdentifier())
                .lessonIdentifier(new LessonIdentifier(lessonResponseModel.getLessonId()))
                .studentIdentifier(new StudentIdentifier(studentResponseModel.getStudentId()))
                .tutorIdentifier(new TutorIdentifier(tutorResponseModel.getTutorId()))
                .lessonSubject(lessonResponseModel.getLessonSubject())
                .studentName(studentResponseModel.getStudentName())
                .tutorName(tutorResponseModel.getTutorName())
                .approval(supervisorConfirmationRequestModel.getApproval())
                .confirmationDate(supervisorConfirmationRequestModel.getConfirmationDate())
                .build();

        SupervisorConfirmation saved = supervisorConfirmationRepository.save(supervisorConfirmation);


        LessonStatus lessonStatus = LessonStatus.SCHEDULED;

        switch (saved.getApproval()){
            case PENDING -> lessonStatus = LessonStatus.SCHEDULED;
            case APPROVED -> lessonStatus = LessonStatus.COMPLETED;
        }


        LessonRequestModel lessonRequestModel = LessonRequestModel.builder()
                .lessonSubject(lessonResponseModel.getLessonSubject())
                .lessonDate(lessonResponseModel.getLessonDate())
                .lessonDuration(lessonResponseModel.getLessonDuration())
                .lessonClassroom(lessonResponseModel.getLessonClassroom())
                .lessonStatus(lessonStatus)
                .streetAddress(lessonResponseModel.getStreetAddress())
                .city(lessonResponseModel.getCity())
                .postalCode(lessonResponseModel.getPostalCode())
                .build();


        lessonServiceClient.updateLessonStatus(lessonRequestModel, lessonResponseModel.getLessonId());

        return supervisorConfirmationResponseModelMapper.entityToResponseModel(saved);
    }

}
