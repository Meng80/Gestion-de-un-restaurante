package com.example.springboot.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DictTests {

    @Test
    void testGetterSetter() {
        // Create a new instance of Dict
        Dict dict = new Dict();

        // Set values using setter methods
        dict.setName("testName");
        dict.setValue("testValue");
        dict.setType("testType");

        // Verify values using getter methods
        assertEquals("testName", dict.getName());
        assertEquals("testValue", dict.getValue());
        assertEquals("testType", dict.getType());
    }

    @Test
    void testToString() {
        // Create a new instance of Dict
        Dict dict = new Dict();
        dict.setName("testName");
        dict.setValue("testValue");
        dict.setType("testType");

        // Verify the toString method
        String expected = "Dict(name=testName, value=testValue, type=testType)";
        assertEquals(expected, dict.toString());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create two instances with the same values
        Dict dict1 = new Dict();
        dict1.setName("testName");
        dict1.setValue("testValue");
        dict1.setType("testType");

        Dict dict2 = new Dict();
        dict2.setName("testName");
        dict2.setValue("testValue");
        dict2.setType("testType");

        // Verify they are equal
        assertEquals(dict1, dict2);

        // Verify hashCode is consistent
        assertEquals(dict1.hashCode(), dict2.hashCode());
    }

    @Test
    void testConstructor() {
        // Create an instance using the constructor and verify values
        Dict dict = new Dict();
        dict.setName("testName");
        dict.setValue("testValue");
        dict.setType("testType");

        assertEquals("testName", dict.getName());
        assertEquals("testValue", dict.getValue());
        assertEquals("testType", dict.getType());
    }
}
