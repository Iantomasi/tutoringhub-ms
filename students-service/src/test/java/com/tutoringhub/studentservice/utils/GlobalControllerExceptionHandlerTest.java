package com.tutoringhub.studentservice.utils;

import com.tutoringhub.studentservice.utils.exceptions.NotFoundException;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import org.hibernate.tool.schema.spi.CommandAcceptanceException;
import org.hibernate.tool.schema.spi.ExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalControllerExceptionHandlerTest {


    @Test
    public void handleNotFoundExceptionTest(){

        GlobalControllerExceptionHandler exceptionHandler = new GlobalControllerExceptionHandler();

        WebRequest request = new ServletWebRequest(new MockHttpServletRequest());
        Exception exception = new NotFoundException("not found");

        HttpErrorInfo httpErrorInfo = exceptionHandler.handleNotFoundException(request, exception);

        assertEquals(HttpStatus.NOT_FOUND , httpErrorInfo.getHttpStatus());
        assertEquals("not found", httpErrorInfo.getMessage());


        }



    }


