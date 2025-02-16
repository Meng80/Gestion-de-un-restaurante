package com.example.springboot.service.impl;

import com.example.springboot.entity.Category;
import com.example.springboot.mapper.CategoryMapper;
import com.example.springboot.service.ICategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CategoryServiceImplTests {

    @Mock
    private CategoryMapper categoryMapper;  // Mocking the CategoryMapper

    @InjectMocks
    private CategoryServiceImpl categoryService;  // Injecting the mocked CategoryMapper into the service

    private Category category;

    @BeforeEach
    public void setUp() {
        // Setup a sample Category object for tests
        category = new Category();
        category.setId(1);
        category.setName("Electronics");
        category.setMark("E01");
    }

    @Test
    public void testSaveCategory() {
        // Arrange: Mock the save behavior of the mapper
        when(categoryMapper.insert(any(Category.class))).thenReturn(1);  // Assuming it returns 1 for a successful insert

        // Act: Call the save method
        boolean result = categoryService.save(category);

        // Assert: Verify the result
        assertTrue(result, "The category should be saved successfully");
        verify(categoryMapper, times(1)).insert(category);  // Verify that insert method was called once with the category
    }

    @Test
    public void testUpdateCategory() {
        // Arrange: Mock the update behavior of the mapper
        when(categoryMapper.updateById(any(Category.class))).thenReturn(1);  // Assuming it returns 1 for a successful update

        // Act: Call the update method
        boolean result = categoryService.updateById(category);

        // Assert: Verify the result
        assertTrue(result, "The category should be updated successfully");
        verify(categoryMapper, times(1)).updateById(category);  // Verify that updateById was called once
    }

    @Test
    public void testGetCategoryById() {
        // Arrange: Mock the selectById behavior
        when(categoryMapper.selectById(1)).thenReturn(category);  // Mocking the behavior of selecting a category by its id

        // Act: Call the method to get category by id
        Category result = categoryService.getById(1);

        // Assert: Verify the result
        assertNotNull(result, "The category should not be null");
        assertEquals(1, result.getId(), "The category ID should match");
        assertEquals("Electronics", result.getName(), "The category name should match");
        assertEquals("E01", result.getMark(), "The category mark should match");

        // Verify the mapper's method was called once with the expected id
        verify(categoryMapper, times(1)).selectById(1);
    }

    @Test
    public void testRemoveCategory() {
        // Arrange: Mock the remove behavior
        when(categoryMapper.deleteById(1)).thenReturn(1);  // Assuming it returns 1 for a successful delete

        // Act: Call the remove method
        boolean result = categoryService.removeById(1);

        // Assert: Verify the result
        assertTrue(result, "The category should be removed successfully");
        verify(categoryMapper, times(1)).deleteById(1);  // Verify that deleteById was called once
    }
}
