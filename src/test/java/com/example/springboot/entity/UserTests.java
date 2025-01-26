package com.example.springboot.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of User
        User user = new User();

        // Set values using setter methods
        user.setId(1);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setNickname("John");
        user.setEmail("john.doe@example.com");
        user.setPhone("1234567890");
        user.setAddress("123 Main St");
        user.setCreateTime(new java.util.Date());
        user.setAvatarUrl("http://example.com/avatar.jpg");
        user.setRole("ROLE_USER");

        // Verify values using getter methods
        assertEquals(1, user.getId());
        assertEquals("john_doe", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("John", user.getNickname());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhone());
        assertEquals("123 Main St", user.getAddress());
        assertNotNull(user.getCreateTime());
        assertEquals("http://example.com/avatar.jpg", user.getAvatarUrl());
        assertEquals("ROLE_USER", user.getRole());
    }

    @Test
    void testToString() {
        // Create a new instance of User
        User user = new User();
        user.setId(1);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setNickname("John");
        user.setEmail("john.doe@example.com");

        // Verify the toString method
        String expected = "User(id=1, username=john_doe, password=password123, nickname=John, email=john.doe@example.com, phone=null, address=null, createTime=null, avatarUrl=null, role=null)";
        assertEquals(expected, user.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances of User with the same values
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("john_doe");

        User user2 = new User();
        user2.setId(1);
        user2.setUsername("john_doe");

        // Verify that the two User objects are equal
        assertEquals(user1, user2);

        // Verify hashCode consistency
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testConstructor() {
        // Create a new User instance and set values
        User user = new User();
        user.setId(2);
        user.setUsername("jane_doe");
        user.setPassword("password456");

        // Verify the fields of the User instance
        assertEquals(2, user.getId());
        assertEquals("jane_doe", user.getUsername());
        assertEquals("password456", user.getPassword());
    }
}
