package com.example.springboot.service.impl;

import com.example.springboot.entity.Product;
import com.example.springboot.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceImplTests {

    @Mock
    private ProductMapper productMapper;  // Mocking the ProductMapper

    @InjectMocks
    private ProductServiceImpl productService;  // Injecting the mocked ProductMapper into the service

    private Product product;

    @BeforeEach
    public void setUp() {
        // Setup a sample Product object for tests
        product = new Product();
        product.setId(1);
        product.setName("Laptop");
        //product.setPrice(1000.0);
    }

    @Test
    public void testSaveProduct() {
        // Arrange: Mock the save behavior of the mapper
        when(productMapper.insert(any(Product.class))).thenReturn(1);  // Assuming it returns 1 for a successful insert

        // Act: Call the save method
        boolean result = productService.save(product);

        // Assert: Verify the result
        assertTrue(result, "The product should be saved successfully");
        verify(productMapper, times(1)).insert(product);  // Verify that insert method was called once with the product
    }

    @Test
    public void testUpdateProduct() {
        // Arrange: Mock the update behavior of the mapper
        when(productMapper.updateById(any(Product.class))).thenReturn(1);  // Assuming it returns 1 for a successful update

        // Act: Call the update method
        boolean result = productService.updateById(product);

        // Assert: Verify the result
        assertTrue(result, "The product should be updated successfully");
        verify(productMapper, times(1)).updateById(product);  // Verify that updateById was called once
    }

    @Test
    public void testGetProductById() {
        // Arrange: Mock the selectById behavior
        when(productMapper.selectById(1)).thenReturn(product);  // Mocking the behavior of selecting a product by its id

        // Act: Call the method to get product by id
        Product result = productService.getById(1);

        // Assert: Verify the result
        assertNotNull(result, "The product should not be null");
        assertEquals(1, result.getId(), "The product ID should match");
        assertEquals("Laptop", result.getName(), "The product name should match");
        //assertEquals(1000.0, result.getPrice(), "The product price should match");

        // Verify the mapper's method was called once with the expected id
        verify(productMapper, times(1)).selectById(1);
    }

    @Test
    public void testRemoveProduct() {
        // Arrange: Mock the remove behavior
        when(productMapper.deleteById(1)).thenReturn(1);  // Assuming it returns 1 for a successful delete

        // Act: Call the remove method
        boolean result = productService.removeById(1);

        // Assert: Verify the result
        assertTrue(result, "The product should be removed successfully");
        verify(productMapper, times(1)).deleteById(1);  // Verify that deleteById was called once
    }
}
