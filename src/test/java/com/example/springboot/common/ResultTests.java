package com.example.springboot.common;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTests {

    @Test
    void testSuccessWithoutData() {
        // Test success without data
        Result result = Result.success();

        // Check if the result has the correct code, message, and null data
        assertEquals(Constants.CODE_200, result.getCode());
        assertEquals("", result.getMsg());
        assertNull(result.getData());
    }

    @Test
    void testSuccessWithData() {
        // Test success with data
        String testData = "Some data";
        Result result = Result.success(testData);

        // Check if the result has the correct code, message, and data
        assertEquals(Constants.CODE_200, result.getCode());
        assertEquals("", result.getMsg());
        assertEquals(testData, result.getData());
    }

    @Test
    void testErrorWithCustomCodeAndMessage() {
        // Test error with custom code and message
        String errorCode = Constants.CODE_500;
        String errorMsg = "Custom error message";
        Result result = Result.error(errorCode, errorMsg);

        // Check if the result has the correct code, message, and null data
        assertEquals(errorCode, result.getCode());
        assertEquals(errorMsg, result.getMsg());
        assertNull(result.getData());
    }

    @Test
    void testErrorWithDefaultValues() {
        // Test error with default code and message
        Result result = Result.error();

        // Check if the result has the default code, message, and null data
        assertEquals(Constants.CODE_500, result.getCode());
        assertEquals("error system", result.getMsg());
        assertNull(result.getData());
    }

}
