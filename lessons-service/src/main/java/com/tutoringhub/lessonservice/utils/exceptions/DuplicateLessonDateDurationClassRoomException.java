package com.tutoringhub.lessonservice.utils.exceptions;

public class DuplicateLessonDateDurationClassRoomException extends RuntimeException {


    public DuplicateLessonDateDurationClassRoomException(){}

    public DuplicateLessonDateDurationClassRoomException(String message){super(message);}

    public DuplicateLessonDateDurationClassRoomException(Throwable cause){super(cause);}

    public DuplicateLessonDateDurationClassRoomException(String message, Throwable cause){super(message, cause); }

}
