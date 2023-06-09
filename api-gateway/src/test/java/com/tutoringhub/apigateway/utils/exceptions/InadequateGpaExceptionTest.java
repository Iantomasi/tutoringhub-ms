package com.tutoringhub.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InadequateGpaExceptionTest {

    @Test
    public void testDefaultConstructor() {
        InadequateGpaException exception = new InadequateGpaException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Inadequate Gpa, must be over 3.5 to tutor";
        InadequateGpaException exception = new InadequateGpaException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        InadequateGpaException exception = new InadequateGpaException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Inadequate Gpa, must be over 3.5 to tutor";
        Throwable cause = new IllegalArgumentException("Invalid Id");
        InadequateGpaException exception = new InadequateGpaException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }




}