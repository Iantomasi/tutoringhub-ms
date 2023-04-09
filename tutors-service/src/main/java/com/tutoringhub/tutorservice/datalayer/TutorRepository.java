package com.tutoringhub.tutorservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TutorRepository extends JpaRepository<Tutor, Integer> {

    Tutor findTutorByTutorIdentifier_TutorId(String tutorId);

}
