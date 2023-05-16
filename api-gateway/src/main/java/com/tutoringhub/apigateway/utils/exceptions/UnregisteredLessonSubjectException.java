package com.tutoringhub.apigateway.utils.exceptions;

public class UnregisteredLessonSubjectException extends RuntimeException{


    public UnregisteredLessonSubjectException(){}

    public UnregisteredLessonSubjectException(String message) { super(message); }

    public UnregisteredLessonSubjectException(Throwable cause){  super(cause);  }

    public UnregisteredLessonSubjectException(String message, Throwable cause){ super(message, cause);   }


}
