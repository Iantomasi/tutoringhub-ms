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
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.InsufficientCommentException;
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupervisorConfirmationServiceImpl implements SupervisorConfirmationService {

    private final SupervisorConfirmationRepository supervisorConfirmationRepository;
    private final SupervisorConfirmationResponseModelMapper supervisorConfirmationResponseModelMapper;
    private final LessonServiceClient lessonServiceClient;
    private final StudentServiceClient studentServiceClient;
    private final TutorServiceClient tutorServiceClient;


//    @Override
//    public List<SupervisorConfirmationResponseModel> getAllSupervisorConfirmations() {
//        List<SupervisorConfirmation> supervisorConfirmations = supervisorConfirmationRepository.findAll();
//        return supervisorConfirmationResponseModelMapper.entityListToResponseModelList(supervisorConfirmations);
//    }

    @Override
    public List<SupervisorConfirmationResponseModel> getStudentExtraCreditReport(String studentId) {
        List<SupervisorConfirmation> studentExtraCreditReport = supervisorConfirmationRepository.findAllSupervisorConfirmationsByStudentIdentifier_studentId(studentId);
        if(studentExtraCreditReport == null){
            throw new NotFoundException("No extra credit report assigned to this studentId");
        }
        return supervisorConfirmationResponseModelMapper.entityListToResponseModelList(studentExtraCreditReport);
    }


//    @Override
//    public SupervisorConfirmationResponseModel getSupervisorConfirmationById(String supervisorConfirmationId) {
//
//        SupervisorConfirmation supervisorConfirmation = supervisorConfirmationRepository.findSupervisorConfirmationBySupervisorConfirmationIdentifier_supervisorConfirmationId(supervisorConfirmationId);
//
//        if (supervisorConfirmation == null) {
//            throw new NotFoundException("Unknown supervisorConfirmationId provided");
//        }
//
//        return supervisorConfirmationResponseModelMapper.entityToResponseModel(supervisorConfirmation);
//    }


    @Override
    public SupervisorConfirmationResponseModel getSupervisorConfirmationByIdInStudentExtraCreditReport(String studentId, String supervisorConfirmationId) {

        SupervisorConfirmation supervisorConfirmation =
                supervisorConfirmationRepository.findSupervisorConfirmationByStudentIdentifier_studentId_AndSupervisorConfirmationIdentifier_supervisorConfirmationId(studentId, supervisorConfirmationId);

        if(supervisorConfirmation == null){
            throw new NotFoundException("No Supervisor Confirmation assigned to this supervisorConfirmationId");
        }
        return supervisorConfirmationResponseModelMapper.entityToResponseModel(supervisorConfirmation);
    }


    @Override
    public SupervisorConfirmationResponseModel processStudentExtraCreditSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId) {


        StudentResponseModel studentResponseModel = studentServiceClient.getStudentAggregate(studentId);
        if (studentResponseModel == null) {
            throw new NotFoundException("Unknown studentId provided: " + studentId);
        }

        LessonResponseModel lessonResponseModel = lessonServiceClient.getLessonAggregate(supervisorConfirmationRequestModel.getLessonId());
        if (lessonResponseModel == null) {
            throw new NotFoundException("Unknown lessonId provided: " + supervisorConfirmationRequestModel.getLessonId());
        }

        TutorResponseModel tutorResponseModel = tutorServiceClient.getTutorAggregate(supervisorConfirmationRequestModel.getTutorId());
        if (tutorResponseModel == null) {
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
                .supervisorComments(supervisorConfirmationRequestModel.getSupervisorComments())
                .build();

        String supervisorComments = supervisorConfirmationRequestModel.getSupervisorComments();
        if (supervisorComments.length() < 10) {
            throw new InsufficientCommentException("Supervisor must provide a comment on the student's efforts and cooperation with tutor of at least 10 characters.");
        }

        SupervisorConfirmation saved = supervisorConfirmationRepository.save(supervisorConfirmation);

        LessonStatus lessonStatus = LessonStatus.SCHEDULED;

        switch (saved.getApproval()) {
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

    @Override
    public SupervisorConfirmationResponseModel updateSupervisorConfirmation(SupervisorConfirmationRequestModel supervisorConfirmationRequestModel, String studentId, String supervisorConfirmationId) {

        SupervisorConfirmation supervisorConfirmation = supervisorConfirmationRepository.findSupervisorConfirmationBySupervisorConfirmationIdentifier_supervisorConfirmationId(supervisorConfirmationId);
        if (supervisorConfirmation == null) {
            throw new NotFoundException("Unknown supervisorConfirmationId provided: " + supervisorConfirmationId);
        }

        StudentResponseModel studentResponseModel = studentServiceClient.getStudentAggregate(studentId);
        if (studentResponseModel == null) {
            throw new NotFoundException("Unknown studentId provided: " + studentId);
        }


        LessonResponseModel lessonResponseModel = lessonServiceClient.getLessonAggregate(supervisorConfirmationRequestModel.getLessonId());
        if (lessonResponseModel == null) {
            throw new NotFoundException("Unknown lessonId provided: " + supervisorConfirmationRequestModel.getLessonId());
        }

        TutorResponseModel tutorResponseModel = tutorServiceClient.getTutorAggregate(supervisorConfirmationRequestModel.getTutorId());
        if (tutorResponseModel == null) {
            throw new NotFoundException("Unknown tutorId provided: " + supervisorConfirmationRequestModel.getTutorId());
        }

        //update supervisorConfirmation

        supervisorConfirmation.setLessonIdentifier(new LessonIdentifier(lessonResponseModel.getLessonId()));
        supervisorConfirmation.setStudentIdentifier(new StudentIdentifier(studentResponseModel.getStudentId()));
        supervisorConfirmation.setTutorIdentifier(new TutorIdentifier(tutorResponseModel.getTutorId()));
        supervisorConfirmation.setLessonSubject(lessonResponseModel.getLessonSubject());
        supervisorConfirmation.setStudentName(studentResponseModel.getStudentName());
        supervisorConfirmation.setTutorName(tutorResponseModel.getTutorName());
        supervisorConfirmation.setApproval(supervisorConfirmationRequestModel.getApproval());
        supervisorConfirmation.setConfirmationDate(supervisorConfirmationRequestModel.getConfirmationDate());
        supervisorConfirmation.setSupervisorComments(supervisorConfirmationRequestModel.getSupervisorComments());

        SupervisorConfirmation updatedSupervisorConfirmation = supervisorConfirmationRepository.save(supervisorConfirmation);

        LessonStatus lessonStatus = LessonStatus.SCHEDULED;

        switch (updatedSupervisorConfirmation.getApproval()) {
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

        return supervisorConfirmationResponseModelMapper.entityToResponseModel(updatedSupervisorConfirmation);
    }

//    @Override
//    public void removeSupervisorConfirmation(String supervisorConfirmationId) {
//
//        SupervisorConfirmation existingSupervisorConfirmation = supervisorConfirmationRepository.findSupervisorConfirmationBySupervisorConfirmationIdentifier_supervisorConfirmationId(supervisorConfirmationId);
//
//        if (existingSupervisorConfirmation == null) {
//            throw new NotFoundException("No SupervisorConfirmation assigned to this studentId and supervisorConfirmationId");
//        } else {
//            supervisorConfirmationRepository.delete(existingSupervisorConfirmation);
//        }
//    }

    @Override
    public void removeStudentSupervisorConfirmation(String studentId, String supervisorConfirmationId) {
        SupervisorConfirmation supervisorConfirmation =
                supervisorConfirmationRepository.findSupervisorConfirmationByStudentIdentifier_studentId_AndSupervisorConfirmationIdentifier_supervisorConfirmationId(studentId, supervisorConfirmationId);

        if(supervisorConfirmation == null){
            throw new NotFoundException("No SupervisorConfirmation assigned to this studentId and supervisorConfirmationId");
        }else{
            supervisorConfirmationRepository.delete(supervisorConfirmation);
        }
    }
}
