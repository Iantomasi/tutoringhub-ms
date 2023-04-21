package com.tutoringhub.studentservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

//@Value
//@Builder
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class StudentResponseModel extends RepresentationModel<StudentResponseModel> {

    private String studentId;
    private String studentName;
    private String studentAge;
    private String studentEmail;
    private String studentSchool;
}
