package com.tutoringhub.studentservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findStudentByStudentIdentifier_StudentId(String studentId);
}
