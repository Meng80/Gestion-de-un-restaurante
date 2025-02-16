package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Product;
import com.example.springboot.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductControllerTests {

    @Mock
    private IProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setCategory("Test Category");
    }

    @Test
    public void testSaveProduct() {
        // Arrange
        when(productService.saveOrUpdate(any(Product.class))).thenReturn(true);

        // Act
        Result result = productController.save(product);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        verify(productService, times(1)).saveOrUpdate(any(Product.class));
    }

    @Test
    public void testDeleteProduct() {
        // Arrange
        when(productService.removeById(1)).thenReturn(true);

        // Act
        Result result = productController.delete(1);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        verify(productService, times(1)).removeById(1);
    }

    @Test
    public void testDeleteBatchProducts() {
        // Arrange
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(productService.removeByIds(ids)).thenReturn(true);

        // Act
        Result result = productController.deleteBatch(ids);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        verify(productService, times(1)).removeByIds(ids);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Product> productList = Arrays.asList(product);
        when(productService.list()).thenReturn(productList);

        // Act
        Result result = productController.findAll();

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        assertEquals(1, ((List<?>) result.getData()).size());
        //assertEquals("Test Product", ((List<?>) result.getData()).get(0).getName());
        verify(productService, times(1)).list();
    }

    @Test
    public void testFindOne() {
        // Arrange
        when(productService.getById(1)).thenReturn(product);

        // Act
        Result result = productController.findOne(1);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        assertEquals("Test Product", ((Product) result.getData()).getName());
        verify(productService, times(1)).getById(1);
    }

    @Test
    public void testFindPage() {
        // Arrange
        List<Product> productList = Arrays.asList(product);
        when(productService.page(any(), any())).thenReturn(new Page<>());

        // Act
        Result result = productController.findPage("Test", "Test Category", 1, 10);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        assertEquals(0, ((Page<?>) result.getData()).getRecords().size());
        //assertEquals("Test Product", ((Product) ((Page<?>) result.getData()).getRecords().get(0)).getName());
        verify(productService, times(1)).page(any(), any());
    }
}
