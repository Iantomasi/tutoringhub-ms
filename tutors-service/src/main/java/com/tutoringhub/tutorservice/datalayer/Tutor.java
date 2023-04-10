package com.tutoringhub.tutorservice.datalayer;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tutors")
@Data
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private TutorIdentifier tutorIdentifier;

    private String tutorName;
    private String tutorAge;
    private String tutorEmail;
    private Double tutorGpa;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @Enumerated(EnumType.STRING)
    private Experience experience;


    Tutor(){ this.tutorIdentifier = new TutorIdentifier(); }

    public Tutor(String tutorName, String tutorAge, String tutorEmail, Double tutorGpa, Specialty specialty, Experience experience) {
        this.tutorIdentifier = new TutorIdentifier();
        this.tutorName = tutorName;
        this.tutorAge = tutorAge;
        this.tutorEmail = tutorEmail;
        this.tutorGpa = tutorGpa;
        this.specialty = specialty;
        this.experience = experience;
    }
}
