package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.tutor;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
