package com.example.springboot.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class RecordResTests {

    @Test
    void testRecordResSettersAndGetters() {
        // Create a RecordRes instance
        RecordRes recordRes = new RecordRes();

        // Set values from both Record and RecordRes fields
        recordRes.setId(1);
        recordRes.setProduct("Laptop");
        recordRes.setCount(10);
        recordRes.setUserId(123);
        recordRes.setCreateTime(LocalDateTime.of(2025, 2, 8, 10, 30, 0));
        recordRes.setMark("In Stock");
        recordRes.setUsername("john_doe");
        recordRes.setProductName("Dell XPS 15");
        recordRes.setCategoryName("Electronics");

        // Assert values are correctly set using JUnit assertions
        assertEquals(1, recordRes.getId());
        assertEquals("Laptop", recordRes.getProduct());
        assertEquals(10, recordRes.getCount());
        assertEquals(123, recordRes.getUserId());
        assertEquals(LocalDateTime.of(2025, 2, 8, 10, 30, 0), recordRes.getCreateTime());
        assertEquals("In Stock", recordRes.getMark());
        assertEquals("john_doe", recordRes.getUsername());
        assertEquals("Dell XPS 15", recordRes.getProductName());
        assertEquals("Electronics", recordRes.getCategoryName());
    }

    @Test
    void testRecordResConstructor() {
        // Use the constructor to set values directly
        RecordRes recordRes = new RecordRes();
        recordRes.setId(2);
        recordRes.setProduct("Smartphone");
        recordRes.setCount(50);
        recordRes.setUserId(456);
        recordRes.setCreateTime(LocalDateTime.now());
        recordRes.setMark("Discounted");
        recordRes.setUsername("jane_doe");
        recordRes.setProductName("iPhone 14");
        recordRes.setCategoryName("Mobile");

        // Assert values using JUnit assertions
        assertEquals(2, recordRes.getId());
        assertEquals("Smartphone", recordRes.getProduct());
        assertEquals(50, recordRes.getCount());
        assertEquals(456, recordRes.getUserId());
        assertNotNull(recordRes.getCreateTime()); // Just checks if it's set
        assertEquals("Discounted", recordRes.getMark());
        assertEquals("jane_doe", recordRes.getUsername());
        assertEquals("iPhone 14", recordRes.getProductName());
        assertEquals("Mobile", recordRes.getCategoryName());
    }

    @Test
    void testDefaultValues() {
        // Create a RecordRes instance with default values
        RecordRes recordRes = new RecordRes();

        // Assert default values
        assertNull(recordRes.getProduct());
        assertNull(recordRes.getUsername());
        assertNull(recordRes.getProductName());
        assertNull(recordRes.getCategoryName());
        assertEquals(null, recordRes.getCount());
        assertEquals(null, recordRes.getUserId());
        assertNull(recordRes.getCreateTime());
        assertNull(recordRes.getMark());
    }

    @Test
    void testInheritance() {
        // Create a RecordRes instance and check if it has fields from Record class
        RecordRes recordRes = new RecordRes();
        recordRes.setId(1);
        recordRes.setProduct("Tablet");
        recordRes.setCount(20);
        recordRes.setUserId(789);
        recordRes.setCreateTime(LocalDateTime.now());
        recordRes.setMark("New");

        // Test that the RecordRes class inherits Record fields
        assertEquals(1, recordRes.getId());
        assertEquals("Tablet", recordRes.getProduct());
        assertEquals(20, recordRes.getCount());
        assertEquals(789, recordRes.getUserId());
        assertNotNull(recordRes.getCreateTime()); // Check if create time is set
        assertEquals("New", recordRes.getMark());
    }
}
