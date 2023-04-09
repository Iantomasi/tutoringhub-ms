package com.tutoringhub.tutorservice.businesslayer;

import com.tutoringhub.tutorservice.datalayer.Tutor;
import com.tutoringhub.tutorservice.datalayer.TutorRepository;
import com.tutoringhub.tutorservice.datamapperlayer.TutorRequestMapper;
import com.tutoringhub.tutorservice.datamapperlayer.TutorResponseMapper;
import com.tutoringhub.tutorservice.presentationlayer.TutorRequestModel;
import com.tutoringhub.tutorservice.presentationlayer.TutorResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorServiceImpl implements TutorService{

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
    public TutorResponseModel getTutorByTutorId(String tutorId) {
        return tutorResponseMapper.entityToResponseModel(tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId));
    }

    @Override
    public TutorResponseModel addTutor(TutorRequestModel tutorRequestModel) {

         Tutor tutor = tutorRequestMapper.requestModelToEntity(tutorRequestModel);
         Tutor saved = tutorRepository.save(tutor);
         return tutorResponseMapper.entityToResponseModel(saved);
    }

    @Override
    public TutorResponseModel updateTutor(TutorRequestModel tutorRequestModel, String tutorId) {

        Tutor existingTutor = tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId);
        if(existingTutor == null){
            return null;
        }

        Tutor tutor = tutorRequestMapper.requestModelToEntity(tutorRequestModel);
        tutor.setId(existingTutor.getId());
        tutor.setTutorIdentifier(existingTutor.getTutorIdentifier());
        return tutorResponseMapper.entityToResponseModel(tutorRepository.save(tutor));
    }

    @Override
    public void removeTutor(String tutorId) {
        Tutor existingTutor = tutorRepository.findTutorByTutorIdentifier_TutorId(tutorId);
        if(existingTutor == null){
            return;
        }
        tutorRepository.delete(existingTutor);
    }
}
