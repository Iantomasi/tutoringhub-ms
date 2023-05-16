package com.tutoringhub.apigateway.presentationlayer.supervisorconfirmation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@Value
public class SupervisorConfirmationRequestModel {

     String lessonId;
     String studentId;
     String tutorId;
     String lessonSubject;
     String studentName;
     String tutorName;
     Approval approval;
     LocalDate confirmationDate;
     String supervisorComments;


}
