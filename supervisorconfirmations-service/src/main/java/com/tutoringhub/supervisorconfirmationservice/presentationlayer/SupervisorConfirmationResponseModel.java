package com.tutoringhub.supervisorconfirmationservice.presentationlayer;

import com.tutoringhub.supervisorconfirmationservice.datalayer.Approval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SupervisorConfirmationResponseModel extends RepresentationModel<SupervisorConfirmationResponseModel> {

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
