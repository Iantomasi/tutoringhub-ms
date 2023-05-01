package com.tutoringhub.apigateway.presentationlayer.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class StudentResponseModel extends RepresentationModel<StudentResponseModel> {

    private String studentId;
    private String studentName;
    private String studentAge;
    private String studentEmail;
    private String studentSchool;
}
