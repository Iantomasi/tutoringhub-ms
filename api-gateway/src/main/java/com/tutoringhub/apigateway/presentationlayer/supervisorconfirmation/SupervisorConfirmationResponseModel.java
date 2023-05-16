package com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SupervisorConfirmationResponseModel {

    private String supervisorConfirmationId;
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
