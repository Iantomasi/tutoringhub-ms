package com.tutoringhub.supervisorconfirmationservice.utils.Exceptions;

public class InsufficientCommentException extends RuntimeException{

    public InsufficientCommentException(){}

    public InsufficientCommentException(String message) { super(message); }

    public InsufficientCommentException(Throwable cause){  super(cause);  }

    public InsufficientCommentException(String message, Throwable cause){ super(message, cause);   }
}