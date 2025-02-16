package com.example.springboot.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryTests {

    @Test
    public void testCategoryConstructorAndGetterSetter() {
        // Arrange
        Category category = new Category();

        // Set values using setters
        category.setId(1);
        category.setName("Electronics");
        category.setMark("E01");

        // Assert the getter values
        assertEquals(1, category.getId());
        assertEquals("Electronics", category.getName());
        assertEquals("E01", category.getMark());
    }

    @Test
    public void testCategoryDefaultValues() {
        // Arrange
        Category category = new Category();

        // Assert default values (they should be null or 0 for primitive types)
        assertNull(category.getName());
        assertNull(category.getMark());
        assertEquals(null, category.getId()); // Since it's an Integer, the default is 0
    }

    @Test
    public void testCategorySetterAndGetterWithNullValues() {
        // Arrange
        Category category = new Category();

        // Set null values
        category.setName(null);
        category.setMark(null);
        category.setId(null);  // The id is Integer (can be null)

        // Assert null values
        assertNull(category.getName());
        assertNull(category.getMark());
        assertNull(category.getId());
    }
}
