package com.tutoringhub.tutorservice.businesslayer;

import com.tutoringhub.tutorservice.datalayer.Tutor;
import com.tutoringhub.tutorservice.datalayer.TutorRepository;
import com.tutoringhub.tutorservice.datamapperlayer.TutorRequestMapper;
import com.tutoringhub.tutorservice.datamapperlayer.TutorResponseMapper;
import com.tutoringhub.tutorservice.presentationlayer.TutorRequestModel;
import com.tutoringhub.tutorservice.presentationlayer.TutorResponseModel;
import com.tutoringhub.tutorservice.utils.exceptions.InadequateGpaException;
import com.tutoringhub.tutorservice.utils.exceptions.NotFoundException;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorServiceImpl implements TutorService {

    private TutorRepository tutorRepository;
    private TutorResponseMapper tutorResponseMapper;
    private TutorRequestMapper tutorRequestMapper;

    public TutorServiceImpl(TutorRepository tutorRepository, TutorResponseMapper tutorResponseMapper, TutorRequestMapper tutorRequestMapper) {
        this.tutorRepository = tutorRepository;
        this.tutorResponseMapper = tutorResponseMapper;
        this.tutorRequestMapper = tutorRequestMapper;
    }

    @Override
    public List<TutorResponseModel> getTutors() {
        return tutorResponseMapper.entityListToResponseModelList(tutorRepository.findAll());
    }

    @Override
    public TutorResponseModel getTutorByTutorId(String tutorId) throws NotFoundException {

        Tutor queriedTutor = tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId);
        if (queriedTutor == null) {
            throw new NotFoundException("No tutor assigned to this tutorId");
        } else{
            return tutorResponseMapper.entityToResponseModel(tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId));
    }
}

    @Override
    public TutorResponseModel addTutor(TutorRequestModel tutorRequestModel) throws InadequateGpaException {

        Tutor tutor = tutorRequestMapper.requestModelToEntity(tutorRequestModel);

        double gpa = tutorRequestModel.getTutorGpa();
        if(gpa<3.5){
            throw new InadequateGpaException("Tutor Gpa must be above 3.5 in order to qualify for giving lessons");
        }else {
            Tutor saved = tutorRepository.save(tutor);
            return tutorResponseMapper.entityToResponseModel(saved);
        }
    }

    @Override
    public TutorResponseModel updateTutor(TutorRequestModel tutorRequestModel, String tutorId) throws NotFoundException, InadequateGpaException  {

        Tutor existingTutor = tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId);
        if (existingTutor == null) {
            throw new NotFoundException("No tutor assigned to this tutorId");
        }

        Tutor tutor = tutorRequestMapper.requestModelToEntity(tutorRequestModel);

        double gpa = tutorRequestModel.getTutorGpa();
        if (gpa < 3.5) {
            throw new InadequateGpaException("Tutor Gpa must be above 3.5 in order to qualify for giving lessons");
        } else {

            tutor.setId(existingTutor.getId());
            tutor.setTutorIdentifier(existingTutor.getTutorIdentifier());
            return tutorResponseMapper.entityToResponseModel(tutorRepository.save(tutor));
        }
    }

    @Override
    public void removeTutor(String tutorId) throws NotFoundException {
        Tutor existingTutor = tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId);
        if (existingTutor == null) {
            throw new NotFoundException("No tutor assigned to this tutorId");
        } else {
            tutorRepository.delete(existingTutor);
        }
    }
}
