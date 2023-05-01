package com.tutoringhub.apigateway.presentationlayer.tutor;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TutorResponseModel {

    private String tutorId;
    private String tutorName;
    private String tutorAge;
    private String tutorEmail;
    private double tutorGpa;
    private Specialty specialty;
    private Experience experience;

}
