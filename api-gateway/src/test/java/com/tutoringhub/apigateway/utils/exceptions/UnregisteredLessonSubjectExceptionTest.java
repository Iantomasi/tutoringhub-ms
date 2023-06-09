package com.tutoringhub.apigateway.utils.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnregisteredLessonSubjectExceptionTest {

    @Test
    public void testDefaultConstructor() {
        UnregisteredLessonSubjectException exception = new UnregisteredLessonSubjectException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructor() {
        String errorMessage = "Unregistered lesson subject. List of subjects: English - French - Math - Science";
        UnregisteredLessonSubjectException exception = new UnregisteredLessonSubjectException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
    @Test
    public void testCauseConstructor() {
        Throwable cause = new Throwable("Test cause");
        UnregisteredLessonSubjectException exception = new UnregisteredLessonSubjectException(cause);
        Assertions.assertEquals("java.lang.Throwable: Test cause", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    @Test
    public void testMessageAndCauseConstructor() {
        String errorMessage = "Unregistered lesson subject. List of subjects: English - French - Math - Science";
        Throwable cause = new IllegalArgumentException("Invalid Id");
        UnregisteredLessonSubjectException exception = new UnregisteredLessonSubjectException(errorMessage, cause);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }



}