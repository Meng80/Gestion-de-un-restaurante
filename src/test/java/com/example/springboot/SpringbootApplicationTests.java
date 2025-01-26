package com.example.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringbootApplicationTests {

    // Test to ensure the Spring Application Context loads successfully
    @Test
    public void contextLoads() {
        // This test will pass if the application context loads without exceptions
    }

    // Test to ensure the main application can start
    @Test
    public void mainApplicationTest() {
        try {
            SpringbootApplication.main(new String[]{}); // Run the main method to start the application
        } catch (Exception e) {
            // If an exception occurs, the test fails
            assert false : "Application failed to start: " + e.getMessage();
        }
    }
}
