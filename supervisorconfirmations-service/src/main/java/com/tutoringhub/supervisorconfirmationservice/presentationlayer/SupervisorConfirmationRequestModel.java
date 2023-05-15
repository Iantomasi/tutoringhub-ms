package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import com.tutoringhub.supervisorconfirmationservice.datalayer.Approval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Value
public class SupervisorConfirmationRequestModel {

    private String lessonId;
    private String studentId;
    private String tutorId;
    private String lessonSubject;
    private String studentName;
    private String tutorName;
    private Approval approval;
    private LocalDate confirmationDate;
    private String supervisorComments;


}
