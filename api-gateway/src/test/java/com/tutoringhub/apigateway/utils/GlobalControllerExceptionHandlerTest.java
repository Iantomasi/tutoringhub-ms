package com.tutoringhub.apigateway.utils;

import com.tutoringhub.apigateway.utils.exceptions.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalControllerExceptionHandlerTest {


    private final GlobalControllerExceptionHandler handler = new GlobalControllerExceptionHandler();

    @Test
    public void testHandleNotFoundException() {
        // Mock the WebRequest and Exception objects
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        NotFoundException ex = new NotFoundException("Not found");

        // Call the method being tested
        HttpErrorInfo errorInfo = handler.handleNotFoundException(request, ex);

        // Check the result
        assertEquals(HttpStatus.NOT_FOUND, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Not found", errorInfo.getMessage());
    }

    @Test
    public void testHandleInvalidInputException() {
        // Set up a mock WebRequest
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        InvalidInputException ex = new InvalidInputException("Invalid input");

        // Call the method being tested
        HttpErrorInfo errorInfo = handler.handleInvalidInputException(request, ex);

        // Check the result
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Invalid input", errorInfo.getMessage());
    }

    @Test
    public void testHandleDuplicateEmailException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        DuplicateEmailException ex = new DuplicateEmailException("Duplicate Email");

        HttpErrorInfo errorInfo = handler.handleDuplicateEmailException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Duplicate Email", errorInfo.getMessage());
    }

    @Test
    public void testHandleInadequateGpaException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        InadequateGpaException ex = new InadequateGpaException("Inadequate Gpa, must be over 3.5 to tutor");

        HttpErrorInfo errorInfo = handler.handleInadequateGpaException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Inadequate Gpa, must be over 3.5 to tutor", errorInfo.getMessage());
    }
    @Test
    public void testInsufficientCommentException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        InsufficientCommentException ex = new InsufficientCommentException("Insufficient comment, must be greater than 5 characters");

        HttpErrorInfo errorInfo = handler.handleInsufficientCommentException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Insufficient comment, must be greater than 5 characters", errorInfo.getMessage());
    }

    @Test
    public void testUnregisteredLessonSubjectException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        UnregisteredLessonSubjectException ex = new UnregisteredLessonSubjectException("Unregistered lesson subject. List of subjects: English - French - Math - Science");

        HttpErrorInfo errorInfo = handler.handleUnregisteredLessonSubjectException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Unregistered lesson subject. List of subjects: English - French - Math - Science", errorInfo.getMessage());
    }



}