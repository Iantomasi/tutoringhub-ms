package com.tutoringhub.supervisorconfirmationservice.datalayer;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "supervisorconfirmations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupervisorConfirmation {

    @Id
    private String id;

    private SupervisorConfirmationIdentifier supervisorConfirmationIdentifier;
    private LessonIdentifier lessonIdentifier;
    private StudentIdentifier studentIdentifier;
    private TutorIdentifier tutorIdentifier;
    private String lessonSubject;
    private String studentName;
    private String tutorName;
    private Approval approval;
    private LocalDate confirmationDate;


}
