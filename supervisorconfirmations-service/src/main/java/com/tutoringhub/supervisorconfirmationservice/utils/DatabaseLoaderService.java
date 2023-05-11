package com.tutoringhub.supervisorconfirmationservice.utils;

import com.tutoringhub.supervisorconfirmationservice.datalayer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DatabaseLoaderService implements CommandLineRunner {


    @Autowired
    SupervisorConfirmationRepository supervisorConfirmationRepository;
    @Override
    public void run(String... args) throws Exception {


        var supervisorConfirmationIdentifier1 = new SupervisorConfirmationIdentifier();
        var lessonIdentifier1 = new LessonIdentifier("b7024d89-1a5e-4517-3gba-05178u7ar262");
        var studentIdentifier1 = new StudentIdentifier("c3540a89-cb47-4c96-888e-ff96708ab1c4");
        var tutorIdentifier1 = new TutorIdentifier("c3540a89-cb47-4c96-888e-ff96708db5d6");


        var supervisorConfirmation1 = SupervisorConfirmation.builder()
                .supervisorConfirmationIdentifier(supervisorConfirmationIdentifier1)
                .lessonIdentifier(lessonIdentifier1)
                .studentIdentifier(studentIdentifier1)
                .tutorIdentifier(tutorIdentifier1)
                .lessonSubject("English")
                .studentName("Cristiano")
                .tutorName("Mourinho")
                .approval(Approval.PENDING)
                .confirmationDate(LocalDate.of(2023, 04, 10))
                .build();




        var supervisorConfirmationIdentifier2 = new SupervisorConfirmationIdentifier();
        var lessonIdentifier2 = new LessonIdentifier("b7024d89-1a5e-4517-3gba-05178u7ar267");
        var studentIdentifier2 = new StudentIdentifier("c3540a89-cb47-4c96-888e-ff96708ab1c7");
        var tutorIdentifier2 = new TutorIdentifier("c3540a89-cb47-4c96-888e-ff96708db5d7");


        var supervisorConfirmation2 = SupervisorConfirmation.builder()
                .supervisorConfirmationIdentifier(supervisorConfirmationIdentifier2)
                .lessonIdentifier(lessonIdentifier2)
                .studentIdentifier(studentIdentifier2)
                .tutorIdentifier(tutorIdentifier2)
                .lessonSubject("Math")
                .studentName("Paulo")
                .tutorName("Jose")
                .approval(Approval.APPROVED)
                .confirmationDate(LocalDate.of(2023, 04, 11))
                .build();


        supervisorConfirmationRepository.insert(supervisorConfirmation1);
        supervisorConfirmationRepository.insert(supervisorConfirmation2);

    }

}


