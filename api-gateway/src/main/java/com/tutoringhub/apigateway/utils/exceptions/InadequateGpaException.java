package com.tutoringhub.apigateway.utils.exceptions;

public class InadequateGpaException extends RuntimeException {

    public InadequateGpaException(){}

    public InadequateGpaException(String message) { super(message); }

    public InadequateGpaException(Throwable cause){  super(cause);  }

    public InadequateGpaException(String message, Throwable cause){ super(message, cause);   }

}