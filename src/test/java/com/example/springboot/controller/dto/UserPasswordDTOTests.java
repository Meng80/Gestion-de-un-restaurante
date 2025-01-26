package com.example.springboot.controller.dto;

import com.example.springboot.controller.dto.UserPasswordDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPasswordDTOTests {

    @Test
    void testGettersAndSetters() {
        // Create a new UserPasswordDTO instance
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();

        // Set properties
        userPasswordDTO.setUsername("john_doe");
        userPasswordDTO.setPhone("123-456-7890");
        userPasswordDTO.setPassword("oldPassword");
        userPasswordDTO.setNewPassword("newPassword123");

        // Test the getters
        assertEquals("john_doe", userPasswordDTO.getUsername());
        assertEquals("123-456-7890", userPasswordDTO.getPhone());
        assertEquals("oldPassword", userPasswordDTO.getPassword());
        assertEquals("newPassword123", userPasswordDTO.getNewPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two UserPasswordDTO instances with identical properties
        UserPasswordDTO userPasswordDTO1 = new UserPasswordDTO();
        userPasswordDTO1.setUsername("john_doe");
        userPasswordDTO1.setPhone("123-456-7890");
        userPasswordDTO1.setPassword("oldPassword");
        userPasswordDTO1.setNewPassword("newPassword123");

        UserPasswordDTO userPasswordDTO2 = new UserPasswordDTO();
        userPasswordDTO2.setUsername("john_doe");
        userPasswordDTO2.setPhone("123-456-7890");
        userPasswordDTO2.setPassword("oldPassword");
        userPasswordDTO2.setNewPassword("newPassword123");

        // Test equals method
        assertEquals(userPasswordDTO1, userPasswordDTO2);

        // Test hashCode method
        assertEquals(userPasswordDTO1.hashCode(), userPasswordDTO2.hashCode());
    }

    @Test
    void testToString() {
        // Create a UserPasswordDTO instance
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setUsername("john_doe");
        userPasswordDTO.setPhone("123-456-7890");
        userPasswordDTO.setPassword("oldPassword");
        userPasswordDTO.setNewPassword("newPassword123");

        // Test the toString method
        String expectedToString = "UserPasswordDTO(username=john_doe, phone=123-456-7890, password=oldPassword, newPassword=newPassword123)";
        assertEquals(expectedToString, userPasswordDTO.toString());
    }
}
