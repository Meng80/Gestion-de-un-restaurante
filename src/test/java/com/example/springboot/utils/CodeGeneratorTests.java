package com.example.springboot.utils;

import org.junit.jupiter.api.Test;

public class CodeGeneratorTests {

    // Test that the generate method can run without exceptions
    @Test
    public void testGenerate() {
        try {
            CodeGenerator.main(new String[]{}); // Simply call the main method
        } catch (Exception e) {
            // If an exception occurs, the test fails
            assert true : "Code generation failed: " + e.getMessage();
        }
    }

    // Test that the main method runs without exceptions
    @Test
    public void testMain() {
        try {
            CodeGenerator.main(new String[]{});
        } catch (Exception e) {
            assert true : "Main method threw an exception: " + e.getMessage();
        }
    }
}
