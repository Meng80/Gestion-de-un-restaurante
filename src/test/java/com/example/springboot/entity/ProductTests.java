package com.example.springboot.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.api.Assertions;

public class ProductTests {

    @Test
    void testProductSettersAndGetters() {
        // Create a Product instance
        Product product = new Product();

        // Set values
        product.setId(1);
        product.setName("Test Product");
        product.setCategory("Electronics");
        product.setCount(100);
        product.setMark("New");

        // Assert the values are set correctly using JUnit Assertions
        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Electronics", product.getCategory());
        assertEquals(100, product.getCount());
        assertEquals("New", product.getMark());
    }

    @Test
    void testProductConstructor() {
        // Use the constructor to set values directly
        Product product = new Product();
        product.setId(2);
        product.setName("Sample Product");
        product.setCategory("Books");
        product.setCount(50);
        product.setMark("Discount");

        // Use AssertJ to make assertions (alternative to JUnit's assertEquals)
        Assertions.assertThat(product.getId()).isEqualTo(2);
        Assertions.assertThat(product.getName()).isEqualTo("Sample Product");
        Assertions.assertThat(product.getCategory()).isEqualTo("Books");
        Assertions.assertThat(product.getCount()).isEqualTo(50);
        Assertions.assertThat(product.getMark()).isEqualTo("Discount");
    }

    @Test
    void testDefaultProduct() {
        // Create a product without setting any properties
        Product product = new Product();

        // Assert that the default values are null or zero
        assertNull(product.getName());
        assertNull(product.getCategory());
        assertEquals(null, product.getCount());
        assertNull(product.getMark());
    }
}
