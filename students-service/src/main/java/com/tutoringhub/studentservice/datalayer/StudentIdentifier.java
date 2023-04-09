package com.tutoringhub.studentservice.datalayer;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class StudentIdentifier {

    private String studentId;

    public StudentIdentifier(){ this.studentId = UUID.randomUUID().toString(); }

    public String getStudentId(){ return studentId; }



}
