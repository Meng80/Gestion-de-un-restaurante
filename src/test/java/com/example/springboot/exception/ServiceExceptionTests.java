package com.example.springboot.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceExceptionTests {

    @Test
    void testServiceExceptionWithCodeAndMessage() {
        // Arrange
        String expectedCode = "404";
        String expectedMessage = "Resource not found";

        // Act
        ServiceException exception = new ServiceException(expectedCode, expectedMessage);

        // Assert
        assertNotNull(exception);
        assertEquals(expectedCode, exception.getCode());
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testServiceExceptionMessage() {
        // Arrange
        String expectedMessage = "An error occurred";

        // Act
        ServiceException exception = new ServiceException("500", expectedMessage);

        // Assert
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testServiceExceptionCode() {
        // Arrange
        String expectedCode = "400";
        String someMessage = "Bad request";

        // Act
        ServiceException exception = new ServiceException(expectedCode, someMessage);

        // Assert
        assertEquals(expectedCode, exception.getCode());
    }
}
