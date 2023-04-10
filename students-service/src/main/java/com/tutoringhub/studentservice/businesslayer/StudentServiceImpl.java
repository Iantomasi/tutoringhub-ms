package com.tutoringhub.studentservice.businesslayer;


import com.tutoringhub.studentservice.datalayer.Student;
import com.tutoringhub.studentservice.datalayer.StudentIdentifier;
import com.tutoringhub.studentservice.datalayer.StudentRepository;
import com.tutoringhub.studentservice.datamapperlayer.StudentRequestMapper;
import com.tutoringhub.studentservice.datamapperlayer.StudentResponseMapper;
import com.tutoringhub.studentservice.presentationlayer.StudentRequestModel;
import com.tutoringhub.studentservice.presentationlayer.StudentResponseModel;
import com.tutoringhub.studentservice.utils.exceptions.DuplicateEmailException;
import com.tutoringhub.studentservice.utils.exceptions.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{

    private StudentRepository studentRepository;
    private StudentResponseMapper studentResponseMapper;
    private StudentRequestMapper studentRequestMapper;

    public StudentServiceImpl(StudentRepository studentRepository, StudentResponseMapper studentResponseMapper, StudentRequestMapper studentRequestMapper) {
        this.studentRepository = studentRepository;
        this.studentResponseMapper = studentResponseMapper;
        this.studentRequestMapper = studentRequestMapper;
    }

    @Override
    public List<StudentResponseModel> getStudents() {
        return studentResponseMapper.entityListToResponseModelList(studentRepository.findAll());
    }

    @Override
    public StudentResponseModel getStudentByStudentId(String studentId) throws NotFoundException {

        Student queriedStudent = studentRepository.findStudentByStudentIdentifier_StudentId(studentId);

        if (queriedStudent == null) {
            throw new NotFoundException("No student assigned to this studentId");
        } else {
            return studentResponseMapper.entityToResponseModel(studentRepository.findStudentByStudentIdentifier_StudentId(studentId));
        }
    }

    @Override
    public StudentResponseModel addStudent(StudentRequestModel studentRequestModel) throws DuplicateEmailException {

        Student student = studentRequestMapper.requestModelToEntity(studentRequestModel);

        String existingEmail = studentRequestModel.getStudentEmail();
        List<Student> foundStudents = studentRepository.findStudentByStudentEmail(existingEmail);

        if (foundStudents.size() > 0) {
            throw new DuplicateEmailException("Email pertaining to more than one student");
        } else {

            Student saved = studentRepository.save(student);
            return studentResponseMapper.entityToResponseModel(saved);
        }
    }

    @Override
    public StudentResponseModel updateStudent(StudentRequestModel studentRequestModel, String studentId) throws NotFoundException {

        Student existingStudent = studentRepository.findStudentByStudentIdentifier_StudentId(studentId);
        if (existingStudent == null) {
            throw new NotFoundException("No student assigned to this studentId");
        } else {
            Student student = studentRequestMapper.requestModelToEntity(studentRequestModel);
            student.setId(existingStudent.getId());
            student.setStudentIdentifier(existingStudent.getStudentIdentifier());
            return studentResponseMapper.entityToResponseModel(studentRepository.save(student));
        }
    }


    @Override
    public void removeStudent(String studentId) throws NotFoundException {
        Student existingStudent = studentRepository.findStudentByStudentIdentifier_StudentId(studentId);
        if (existingStudent == null) {
            throw new NotFoundException("No student assigned to this studentId");
        } else {
            studentRepository.delete(existingStudent);
        }
    }
}
