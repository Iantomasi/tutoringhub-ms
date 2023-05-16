package com.tutoringhub.apigateway.presentationlayer.student;

import lombok.*;

@EqualsAndHashCode(callSuper=false)
@Data
@Builder
@AllArgsConstructor
public class StudentRequestModel {

     private String studentName;
     private String studentAge;
     private String studentEmail;
     private String studentSchool;


}
