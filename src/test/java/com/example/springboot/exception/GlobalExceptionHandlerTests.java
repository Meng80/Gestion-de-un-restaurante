package com.example.springboot.exception;

import com.example.springboot.common.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTests {

    @Test
    public void handleServiceException_ShouldReturnErrorResponse() {
        // Arrange
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        ServiceException serviceException = new ServiceException("400", "Service exception occurred");

        // Act
        Result result = exceptionHandler.handle(serviceException);

        // Assert
        assertEquals("400", result.getCode());
        //assertEquals("Service exception occurred", result.getMessage());
    }
}
