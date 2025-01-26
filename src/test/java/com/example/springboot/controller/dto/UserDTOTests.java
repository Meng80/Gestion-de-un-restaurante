package com.example.springboot.controller.dto;

import com.example.springboot.entity.Menu;
import com.example.springboot.controller.dto.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class UserDTOTests {

    @Test
    void testGettersAndSetters() {
        // Create a new UserDTO instance
        UserDTO userDTO = new UserDTO();

        // Set properties
        userDTO.setUsername("john_doe");
        userDTO.setPassword("password123");
        userDTO.setNickname("John");
        userDTO.setAvatarUrl("http://example.com/avatar.jpg");
        userDTO.setToken("token123");
        userDTO.setRole("ADMIN");

        // Create a sample menu list
        Menu menu1 = new Menu();
        menu1.setId(1);
        menu1.setName("Dashboard");
        List<Menu> menus = Arrays.asList(menu1);

        userDTO.setMenus(menus);

        // Test the getters
        assertEquals("john_doe", userDTO.getUsername());
        assertEquals("password123", userDTO.getPassword());
        assertEquals("John", userDTO.getNickname());
        assertEquals("http://example.com/avatar.jpg", userDTO.getAvatarUrl());
        assertEquals("token123", userDTO.getToken());
        assertEquals("ADMIN", userDTO.getRole());
        assertEquals(menus, userDTO.getMenus());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two UserDTO instances with identical properties
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setUsername("john_doe");
        userDTO1.setPassword("password123");

        UserDTO userDTO2 = new UserDTO();
        userDTO2.setUsername("john_doe");
        userDTO2.setPassword("password123");

        // Test equals method
        assertEquals(userDTO1, userDTO2);

        // Test hashCode method
        assertEquals(userDTO1.hashCode(), userDTO2.hashCode());
    }

    @Test
    void testToString() {
        // Create a UserDTO instance
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("john_doe");
        userDTO.setPassword("password123");

        // Test the toString method
        String expectedToString = "UserDTO(username=john_doe, password=password123, nickname=null, avatarUrl=null, token=null, role=null, menus=null)";
        assertEquals(expectedToString, userDTO.toString());
    }
}
