package com.tutoringhub.supervisorconfirmationservice.businesslayer;

import com.tutoringhub.supervisorconfirmationservice.datalayer.*;
import com.tutoringhub.supervisorconfirmationservice.datamapperlayer.SupervisorConfirmationResponseModelMapper;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonResponseModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonServiceClient;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.lesson.LessonStatus;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.student.StudentResponseModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.student.StudentServiceClient;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor.Experience;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor.Specialty;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor.TutorResponseModel;
import com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor.TutorServiceClient;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationRequestModel;
import com.tutoringhub.supervisorconfirmationservice.presentationlayer.SupervisorConfirmationResponseModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
class SupervisorConfirmationServiceImplTest {

    @Autowired
    SupervisorConfirmationService supervisorConfirmationService;

    @MockBean
    StudentServiceClient studentServiceClient;

    @MockBean
    TutorServiceClient tutorServiceClient;

    @MockBean
    LessonServiceClient lessonServiceClient;

    @MockBean
    SupervisorConfirmationRepository supervisorConfirmationRepository;

    @SpyBean
    SupervisorConfirmationResponseModelMapper supervisorConfirmationResponseModelMapper;

    @Test
    void getStudentExtraCreditReport_ShouldReturnAllSupervisorConfirmations() {

        String studentId = "studentId";

        SupervisorConfirmation confirmation1 = buildConfirmation1();
        SupervisorConfirmation confirmation2 = buildConfirmation1();

        List<SupervisorConfirmation> confirmations = new ArrayList<>(Arrays.asList(confirmation1, confirmation2));

        when(supervisorConfirmationRepository.findAllSupervisorConfirmationsByStudentIdentifier_studentId(studentId)).thenReturn(confirmations);

        List<SupervisorConfirmationResponseModel> confirmationResponseModels = supervisorConfirmationService.getStudentExtraCreditReport(studentId);

        assertNotNull(confirmationResponseModels);
        assertEquals(2, confirmationResponseModels.size());

        verify(supervisorConfirmationRepository, times(1)).findAllSupervisorConfirmationsByStudentIdentifier_studentId(studentId);
        verify(supervisorConfirmationResponseModelMapper, times(1)).entityListToResponseModelList(confirmations);
    }


    @Test
    void getSupervisorConfirmationInStudentExtraCreditReportById_SupervisorConfirmationId() {
        String studentId = "studentId";
        String supervisorConfirmationId = "supervisorConfirmationId";

        SupervisorConfirmation confirmation = buildConfirmation2();

        when(supervisorConfirmationRepository.findSupervisorConfirmationByStudentIdentifier_studentId_AndSupervisorConfirmationIdentifier_supervisorConfirmationId(studentId,supervisorConfirmationId)).thenReturn(confirmation);

        SupervisorConfirmationResponseModel confirmationResponseModel = supervisorConfirmationService.getSupervisorConfirmationByIdInStudentExtraCreditReport(studentId, supervisorConfirmationId);

        assertNotNull(confirmationResponseModel);
        assertEquals(supervisorConfirmationId, confirmationResponseModel.getSupervisorConfirmationId());

        verify(supervisorConfirmationRepository, times(1)).findSupervisorConfirmationByStudentIdentifier_studentId_AndSupervisorConfirmationIdentifier_supervisorConfirmationId(studentId,supervisorConfirmationId);
        verify(supervisorConfirmationResponseModelMapper, times(1)).entityToResponseModel(confirmation);
    }

    @Test
    void updateSupervisorConfirmation() {

        String studentId = "studentID";
        String supervisorConfirmationId = "gef3b186-9688-42d8-ad47-5fd3b2c63b7a";

        StudentResponseModel studentResponseModel = new StudentResponseModel(studentId, "Neymar", "21", "njr@gmail.com", "Champlain College Saint-Lambert");
        LessonResponseModel lessonResponseModel = new LessonResponseModel("b7024d89-1a5e-4517-3gba-05178u7ar260", "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        TutorResponseModel tutorResponseModel = new TutorResponseModel("c3540a89-cb47-4c96-888e-ff96708db5d5", "Xavi", "26", "viscabarca@gmail.com",4.0, Specialty.English, Experience.MidLevel);

        SupervisorConfirmationRequestModel confirmationRequestModel = SupervisorConfirmationRequestModel.builder()
                .lessonId("b7024d89-1a5e-4517-3gba-05178u7ar260")
                .studentId("c3540a89-cb47-4c96-888e-ff96708ab1c3")
                .tutorId("c3540a89-cb47-4c96-888e-ff96708db5d5")
                .lessonSubject("English")
                .studentName("Leo")
                .tutorName("Messi")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 06, 17))
                .supervisorComments("To be evaluated")
                .build();


        SupervisorConfirmation existingConfirmation = buildConfirmation2();

        when(supervisorConfirmationRepository.findSupervisorConfirmationBySupervisorConfirmationIdentifier_supervisorConfirmationId(supervisorConfirmationId)).thenReturn(existingConfirmation);
        when(studentServiceClient.getStudentAggregate(studentId)).thenReturn(studentResponseModel);
        when(lessonServiceClient.getLessonAggregate(confirmationRequestModel.getLessonId())).thenReturn(lessonResponseModel);
        when(tutorServiceClient.getTutorAggregate(confirmationRequestModel.getTutorId())).thenReturn(tutorResponseModel);
        when(supervisorConfirmationRepository.save(any(SupervisorConfirmation.class))).thenReturn(existingConfirmation);

        // Act
        SupervisorConfirmationResponseModel confirmationResponseModel = supervisorConfirmationService.updateSupervisorConfirmation(confirmationRequestModel,studentId,supervisorConfirmationId);

        // Assert
        assertNotNull(confirmationResponseModel);
        assertNotNull(confirmationResponseModel.getSupervisorConfirmationId());
        assertEquals(studentId, confirmationResponseModel.getStudentId());
        assertEquals(confirmationRequestModel.getLessonId(), confirmationResponseModel.getLessonId());
        assertEquals(confirmationRequestModel.getTutorId(), confirmationResponseModel.getTutorId());
        assertEquals(lessonResponseModel.getLessonSubject(), confirmationResponseModel.getLessonSubject());
        assertEquals(studentResponseModel.getStudentName(), confirmationResponseModel.getStudentName());
        assertEquals(tutorResponseModel.getTutorName(), confirmationResponseModel.getTutorName());
        assertEquals(confirmationRequestModel.getApproval(), confirmationResponseModel.getApproval());
        assertEquals(confirmationRequestModel.getConfirmationDate(), confirmationResponseModel.getConfirmationDate());
        assertEquals(confirmationRequestModel.getSupervisorComments(), confirmationResponseModel.getSupervisorComments());

        verify(supervisorConfirmationResponseModelMapper, times(1)).entityToResponseModel(existingConfirmation);

    }


    @Test
    void whenValidSupervisorConfirmationFields_thenProcessSupervisorConfirmation_ShouldSucceed(){

        //arrange
        SupervisorConfirmationRequestModel confirmationRequestModel = SupervisorConfirmationRequestModel.builder()
                .lessonId("lessonId")
                .studentId("studentId")
                .tutorId("tutorId")
                .lessonSubject("English")
                .studentName("Leo")
                .tutorName("Messi")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 06, 17))
                .supervisorComments("To be evaluated")
                .build();


        String studentId = "studentId";


        StudentResponseModel studentResponseModel = new StudentResponseModel("studentId", "Leo", "21", "lm10@gmail.com", "Champlain College Saint-Lambert");
        LessonResponseModel lessonResponseModel = new LessonResponseModel("lessonId", "English", "April 20 2023", "8:00-9:30", "B202", LessonStatus.SCHEDULED, "900 Riverside Drive", "Saint-Lambert", "J6V 1S4");
        TutorResponseModel tutorResponseModel = new TutorResponseModel("tutorId", "Messi", "26", "viscabarca@gmail.com",4.0, Specialty.English, Experience.MidLevel);

        //mock
        SupervisorConfirmation confirmation = buildConfirmation1();
        SupervisorConfirmation saved = buildConfirmation1();
        saved.setId("0001");


        //define mock behaviors
        when(studentServiceClient.getStudentAggregate(studentId)).thenReturn(studentResponseModel);
        when(tutorServiceClient.getTutorAggregate(confirmationRequestModel.getTutorId())).thenReturn(tutorResponseModel);
        when(lessonServiceClient.getLessonAggregate(confirmationRequestModel.getLessonId())).thenReturn(lessonResponseModel);

        when(supervisorConfirmationRepository.save(any(SupervisorConfirmation.class))).thenReturn(saved);


        //act
        SupervisorConfirmationResponseModel confirmationResponseModel = supervisorConfirmationService.processStudentExtraCreditSupervisorConfirmation(confirmationRequestModel, studentId);

        //assert
        assertNotNull(confirmationResponseModel);
        assertNotNull(confirmationResponseModel.getSupervisorConfirmationId());
        assertEquals(studentId, confirmationResponseModel.getStudentId());
        assertEquals(confirmationRequestModel.getLessonId(), confirmationResponseModel.getLessonId());
        assertEquals(confirmationRequestModel.getTutorId(), confirmationResponseModel.getTutorId());
        assertEquals(lessonResponseModel.getLessonSubject(), confirmationResponseModel.getLessonSubject());
        assertEquals(studentResponseModel.getStudentName(), confirmationResponseModel.getStudentName());
        assertEquals(tutorResponseModel.getTutorName(), confirmationResponseModel.getTutorName());
        assertEquals(confirmationRequestModel.getApproval(), confirmationResponseModel.getApproval());
        assertEquals(confirmationRequestModel.getConfirmationDate(), confirmationResponseModel.getConfirmationDate());
        assertEquals(confirmationRequestModel.getSupervisorComments(), confirmationResponseModel.getSupervisorComments());

        //for the spy
        verify(supervisorConfirmationResponseModelMapper, times(1)).entityToResponseModel(saved);
    }



    @Test
    void removeSupervisorConfirmationId() {
        String studentId = "studentId";
        String supervisorConfirmationId = "supervisorConfirmationId";

        SupervisorConfirmation confirmation = buildConfirmation2();

        when(supervisorConfirmationRepository.findSupervisorConfirmationByStudentIdentifier_studentId_AndSupervisorConfirmationIdentifier_supervisorConfirmationId(studentId,supervisorConfirmationId)).thenReturn(confirmation);
        doNothing().when(supervisorConfirmationRepository).delete(confirmation);

        supervisorConfirmationService.removeStudentSupervisorConfirmation(studentId, supervisorConfirmationId);

        verify(supervisorConfirmationRepository, times(1)).findSupervisorConfirmationByStudentIdentifier_studentId_AndSupervisorConfirmationIdentifier_supervisorConfirmationId(studentId,supervisorConfirmationId);
        verify(supervisorConfirmationRepository, times(1)).delete(confirmation);
    }







    private SupervisorConfirmation buildConfirmation1() {


        var supervisorConfirmationIdentifier1 = new SupervisorConfirmationIdentifier();
        var lessonIdentifier1 = new LessonIdentifier("lessonId");
        var studentIdentifier1 = new StudentIdentifier("studentId");
        var tutorIdentifier1 = new TutorIdentifier("tutorId");

        var confirmation1 = SupervisorConfirmation.builder()
                .supervisorConfirmationIdentifier(supervisorConfirmationIdentifier1)
                .lessonIdentifier(lessonIdentifier1)
                .studentIdentifier(studentIdentifier1)
                .tutorIdentifier(tutorIdentifier1)
                .lessonSubject("English")
                .studentName("Leo")
                .tutorName("Messi")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 06, 17))
                .supervisorComments("To be evaluated")
                .build();
        return confirmation1;
    }

    private SupervisorConfirmation buildConfirmation2() {


        var supervisorConfirmationIdentifier1 = new SupervisorConfirmationIdentifier("supervisorConfirmationId");
        var lessonIdentifier1 = new LessonIdentifier("lessonId");
        var studentIdentifier1 = new StudentIdentifier("studentId");
        var tutorIdentifier1 = new TutorIdentifier("tutorId");

        var confirmation1 = SupervisorConfirmation.builder()
                .supervisorConfirmationIdentifier(supervisorConfirmationIdentifier1)
                .lessonIdentifier(lessonIdentifier1)
                .studentIdentifier(studentIdentifier1)
                .tutorIdentifier(tutorIdentifier1)
                .lessonSubject("English")
                .studentName("Leo")
                .tutorName("Messi")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 06, 17))
                .supervisorComments("To be evaluated")
                .build();
        return confirmation1;
    }



}