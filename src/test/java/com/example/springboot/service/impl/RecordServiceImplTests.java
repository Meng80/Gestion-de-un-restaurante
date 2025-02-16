package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.RecordRes;
import com.example.springboot.mapper.RecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RecordServiceImplTests {

    @Mock
    private RecordMapper recordMapper;  // Mocking the RecordMapper

    @InjectMocks
    private RecordServiceImpl recordService;  // Injecting the mocked RecordMapper into the service

    private Page<RecordRes> page;
    private QueryWrapper<Record> queryWrapper;

    @BeforeEach
    public void setUp() {
        page = new Page<>(1, 10);  // Creating a page object (1st page, 10 records per page)
        queryWrapper = new QueryWrapper<>();  // Creating a basic query wrapper
    }

    @Test
    public void testFindRecordPage() {
        // Arrange
        Page<RecordRes> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Arrays.asList(new RecordRes(), new RecordRes()));  // Using Arrays.asList() to create a list of records

        // Mock the behavior of recordMapper.pageCC
        when(recordMapper.pageCC(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        Page<RecordRes> result = recordService.findRecordPage(page, queryWrapper);

        // Assert
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(2, result.getRecords().size());  // Ensure the result has the expected number of records
        assertEquals(1, result.getCurrent());  // Ensure the current page is correct
        assertEquals(10, result.getSize());  // Ensure the page size is correct
    }

    @Test
    public void testFindRecordPageNoRecords() {
        // Arrange
        Page<RecordRes> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Arrays.asList());  // No records returned using Arrays.asList()

        // Mock the behavior of recordMapper.pageCC
        when(recordMapper.pageCC(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        Page<RecordRes> result = recordService.findRecordPage(page, queryWrapper);

        // Assert
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(0, result.getRecords().size());  // Ensure no records are returned
    }

    @Test
    public void testFindRecordPageWithQueryWrapper() {
        // Arrange
        Page<RecordRes> mockPage = new Page<>(1, 10);
        mockPage.setRecords(Arrays.asList(new RecordRes()));  // Using Arrays.asList() to mock the records

        // Create a custom query wrapper (for example filtering by some condition)
        queryWrapper.eq("name", "testName");

        // Mock the behavior of recordMapper.pageCC
        when(recordMapper.pageCC(any(Page.class), eq(queryWrapper))).thenReturn(mockPage);

        // Act
        Page<RecordRes> result = recordService.findRecordPage(page, queryWrapper);

        // Assert
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(1, result.getRecords().size());  // Ensure only 1 record is returned
        verify(recordMapper, times(1)).pageCC(any(Page.class), eq(queryWrapper));  // Verify that the method was called with the correct queryWrapper
    }
}
