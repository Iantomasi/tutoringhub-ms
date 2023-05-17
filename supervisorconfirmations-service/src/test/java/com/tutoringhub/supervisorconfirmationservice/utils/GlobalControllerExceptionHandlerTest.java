package com.tutoringhub.supervisorconfirmationservice.utils;

import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.InsufficientCommentException;
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.InvalidInputException;
import com.tutoringhub.supervisorconfirmationservice.utils.Exceptions.NotFoundException;
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
    public void testInsufficientCommentException() {
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("test/path");
        InsufficientCommentException ex = new InsufficientCommentException("Insufficient comment, must be greater than 5 characters");

        HttpErrorInfo errorInfo = handler.handleInsufficientCommentException(request, ex);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, errorInfo.getHttpStatus());
        assertEquals("test/path", errorInfo.getPath());
        assertEquals("Insufficient comment, must be greater than 5 characters", errorInfo.getMessage());
    }


}