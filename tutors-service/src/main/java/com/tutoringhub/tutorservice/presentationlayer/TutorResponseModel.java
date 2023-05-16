package com.tutoringhub.tutorservice.presentationlayer;


import com.tutoringhub.tutorservice.datalayer.Experience;
import com.tutoringhub.tutorservice.datalayer.Specialty;
import lombok.*;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TutorResponseModel {

    private String tutorId;
    private String tutorName;
    private String tutorAge;
    private String tutorEmail;
    private double tutorGpa;
    private Specialty specialty;
    private Experience experience;

}
