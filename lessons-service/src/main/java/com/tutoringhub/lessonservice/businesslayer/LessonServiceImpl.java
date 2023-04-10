package com.tutoringhub.lessonservice.businesslayer;


import com.tutoringhub.lessonservice.datalayer.Address;
import com.tutoringhub.lessonservice.datalayer.Lesson;
import com.tutoringhub.lessonservice.datalayer.LessonRepository;
import com.tutoringhub.lessonservice.datamapperlayer.LessonRequestMapper;
import com.tutoringhub.lessonservice.datamapperlayer.LessonResponseMapper;
import com.tutoringhub.lessonservice.presentationlayer.LessonRequestModel;
import com.tutoringhub.lessonservice.presentationlayer.LessonResponseModel;
import com.tutoringhub.lessonservice.utils.exceptions.NotFoundException;
import com.tutoringhub.lessonservice.utils.exceptions.UnregisteredLessonSubjectException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService{


    private LessonRepository lessonRepository;
    private LessonResponseMapper lessonResponseMapper;
    private LessonRequestMapper lessonRequestMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, LessonResponseMapper lessonResponseMapper, LessonRequestMapper lessonRequestMapper) {
        this.lessonRepository = lessonRepository;
        this.lessonResponseMapper = lessonResponseMapper;
        this.lessonRequestMapper = lessonRequestMapper;
    }

    @Override
    public List<LessonResponseModel> getLessons() {
        return lessonResponseMapper.entityListToResponseModelList(lessonRepository.findAll());
    }

    @Override
    public LessonResponseModel getLessonByLessonId(String lessonId) throws NotFoundException{

        Lesson queriedLesson = lessonRepository.findLessonByLessonIdentifier_LessonId(lessonId);
        if (queriedLesson == null) {
            throw new NotFoundException("No lesson assigned to this lessonId");
        } else {
            return lessonResponseMapper.entityToResponseModel(lessonRepository.findLessonByLessonIdentifier_LessonId(lessonId));
        }
    }

    @Override
    public LessonResponseModel addLesson(LessonRequestModel lessonRequestModel) throws UnregisteredLessonSubjectException{

        Address address = new Address(lessonRequestModel.getStreetAddress(), lessonRequestModel.getCity(),
                lessonRequestModel.getPostalCode());
        Lesson lesson = lessonRequestMapper.requestModelToEntity(lessonRequestModel);
        lesson.setAddress(address);

        String validLessonSubjects = lesson.getLessonSubject();

        if (!validLessonSubjects.equals("English") && !validLessonSubjects.equals("French") && !validLessonSubjects.equals("Math") && !validLessonSubjects.equals("Science")) {
            throw new UnregisteredLessonSubjectException("The only registered lesson subjects are English, French, Math or Science");
        } else {
            return lessonResponseMapper.entityToResponseModel(lessonRepository.save(lesson));
        }
    }

    @Override
    public LessonResponseModel updateLesson(LessonRequestModel lessonRequestModel, String lessonId) throws NotFoundException,UnregisteredLessonSubjectException {

        Lesson existingLesson = lessonRepository.findLessonByLessonIdentifier_LessonId(lessonId);

        if (existingLesson == null) {
            throw new NotFoundException("No lesson assigned to this lessonId");
        } else {

            Address address = new Address(lessonRequestModel.getStreetAddress(), lessonRequestModel.getCity(),
                    lessonRequestModel.getPostalCode());

            Lesson lesson = lessonRequestMapper.requestModelToEntity(lessonRequestModel);
            lesson.setAddress(address);
            lesson.setId(existingLesson.getId());
            lesson.setLessonIdentifier(existingLesson.getLessonIdentifier());

            String validUpdatedLessonSubjects = lesson.getLessonSubject();

            if (!validUpdatedLessonSubjects.equals("English") && !validUpdatedLessonSubjects.equals("French") && !validUpdatedLessonSubjects.equals("Math") && !validUpdatedLessonSubjects.equals("Science")) {
                throw new UnregisteredLessonSubjectException("The only registered lesson subjects are English, French, Math or Science");
            } else {
                return lessonResponseMapper.entityToResponseModel(lessonRepository.save(lesson));
            }
        }
    }

    @Override
    public void removeLesson(String lessonId) throws NotFoundException {

        Lesson existingLesson = lessonRepository.findLessonByLessonIdentifier_LessonId(lessonId);

        if (existingLesson == null) {
            throw new NotFoundException("No lesson assigned to this lessonId");
        } else {
            lessonRepository.delete(existingLesson);
        }
    }

}
