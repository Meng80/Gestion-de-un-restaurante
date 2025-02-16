package com.example.springboot.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.api.Assertions;

public class RecordTests {

    @Test
    void testRecordSettersAndGetters() {
        // Create a Record instance
        Record record = new Record();

        // Set values
        record.setId(1);
        record.setProduct("Laptop");
        record.setCount(10);
        record.setUserId(123);
        record.setCreateTime(LocalDateTime.of(2025, 2, 8, 10, 30, 0));
        record.setMark("In Stock");
        record.setAction("Added");

        // Assert the values are set correctly using JUnit Assertions
        assertEquals(1, record.getId());
        assertEquals("Laptop", record.getProduct());
        assertEquals(10, record.getCount());
        assertEquals(123, record.getUserId());
        assertEquals(LocalDateTime.of(2025, 2, 8, 10, 30, 0), record.getCreateTime());
        assertEquals("In Stock", record.getMark());
        assertEquals("Added", record.getAction());
    }

    @Test
    void testRecordDefaultValues() {
        // Create a Record without setting any properties
        Record record = new Record();

        // Assert default values (action should be null as it's not persisted in DB)
        assertNull(record.getProduct());
        assertNull(record.getMark());
        assertNull(record.getAction());
        assertNull(record.getCreateTime());
        assertEquals(null, record.getCount());
        assertEquals(null, record.getUserId());
    }

    @Test
    void testCreateTime() {
        // Create a Record instance with a specific creation time
        LocalDateTime currentTime = LocalDateTime.now();
        Record record = new Record();
        record.setCreateTime(currentTime);

        // Assert that the create time is set correctly
        assertEquals(currentTime, record.getCreateTime());
    }

    @Test
    void testActionField() {
        // Test the 'action' field which is not persisted in DB
        Record record = new Record();
        record.setAction("Updated");

        // Assert that the 'action' field is correctly set
        assertEquals("Updated", record.getAction());
    }
}
