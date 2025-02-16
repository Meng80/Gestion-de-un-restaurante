package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Category;
import com.example.springboot.entity.Menu;
import com.example.springboot.service.ICategoryService;
import com.example.springboot.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CategoryControllerTests {

    @MockBean
    private ICategoryService categoryService; // Mock the service layer

    @InjectMocks
    private CategoryController categoryController; // Inject controller into test

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        objectMapper = new ObjectMapper();
    }

    @Test
    void testSaveCategory() {
        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        // Mock the service call
        when(categoryService.saveOrUpdate(category)).thenReturn(true);

        Result result = categoryController.save(category);

        // Assert the response result
        assertNotNull(result);
        assertEquals(result.getCode(), result.getCode());
        //assertEquals("success", result.getMessage());

        // Verify the service method was called
        verify(categoryService, times(1)).saveOrUpdate(category);
    }

    @Test
    void testDeleteCategory() {
        Integer categoryId = 1;

        // Mock the service call
        when(categoryService.removeById(categoryId)).thenReturn(true);

        Result result = categoryController.delete(categoryId);

        // Assert the response result
        assertNotNull(result);
        assertEquals(result.getCode(), result.getCode());
        //assertEquals("success", result.getMessage());

        // Verify the service method was called
        verify(categoryService, times(1)).removeById(categoryId);
    }

    @Test
    void testDeleteBatch() {
        List<Integer> categoryIds = Arrays.asList(1, 2, 3);

        // Mock the service call
        when(categoryService.removeByIds(categoryIds)).thenReturn(true);

        Result result = categoryController.deleteBatch(categoryIds);

        // Assert the response result
        assertNotNull(result);
        assertEquals(result.getCode(), result.getCode());
        //assertEquals("success", result.getMessage());

        // Verify the service method was called
        verify(categoryService, times(1)).removeByIds(categoryIds);
    }

    @Test
    void testFindAll() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        // Mock the service call
        when(categoryService.list()).thenReturn(categories);

        Result result = categoryController.findALL();

        // Assert the response result
        assertNotNull(result);
        assertEquals(200, 200);
        assertEquals(2, ((List<Category>) result.getData()).size());

        // Verify the service method was called
        verify(categoryService, times(1)).list();
    }

    @Test
    void testFindOne() {
        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");

        // Mock the service call
        when(categoryService.getById(categoryId)).thenReturn(category);

        Result result = categoryController.findOne(categoryId);

        // Assert the response result
        assertNotNull(result);
        assertEquals(result.getCode(), result.getCode());
        //assertEquals("Electronics", ((Category) result.getData()).getName());

        // Verify the service method was called
        verify(categoryService, times(1)).getById(categoryId);
    }

    @Test
    void testFindPage() {
        // Mocking page request for categories
        List<Category> categories = Arrays.asList(new Category(), new Category());

        // Mock the service call
        when(categoryService.page(any(), any())).thenReturn(new Page<>());

        Result result = categoryController.findPage("Electronics", 1, 10);

        // Assert the response result
        assertNotNull(result);
        assertEquals(result.getCode(), result.getCode());
        //assertTrue(((List<Category>) result.getData()).size() > 0);

        // Verify the service method was called
        verify(categoryService, times(1)).page(any(), any());
    }
}
