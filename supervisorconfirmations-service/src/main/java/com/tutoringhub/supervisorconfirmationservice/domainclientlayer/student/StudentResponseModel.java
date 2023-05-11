package com.tutoringhub.supervisorconfirmationservice.domainclientlayer.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StudentResponseModel {

    private String studentId;
    private String studentName;
    private String studentAge;
    private String studentEmail;
    private String studentSchool;
}
