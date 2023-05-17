package com.tutoringhub.supervisorconfirmationservice.utils.Exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsufficientCommentExceptionTest {
    @Test
    public void testDefaultConstructor() {
        InsufficientCommentException exception = new InsufficientCommentException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Insufficient comment, must be greater than 5 characters";
        InsufficientCommentException exception = new InsufficientCommentException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        InsufficientCommentException exception = new InsufficientCommentException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Insufficient comment, must be greater than 5 characters";
        Throwable cause = new IllegalArgumentException("Invalid Id");
        InsufficientCommentException exception = new InsufficientCommentException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}