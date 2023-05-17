package com.tutoringhub.apigateway.presentationlayer.tutor;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@EqualsAndHashCode(callSuper = false)
@Value
public class TutorResponseModel extends RepresentationModel<TutorResponseModel> {

    private String tutorId;
    private String tutorName;
    private String tutorAge;
    private String tutorEmail;
    private double tutorGpa;
    private Specialty specialty;
    private Experience experience;

}
