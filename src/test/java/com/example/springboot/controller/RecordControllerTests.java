package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Product;
import com.example.springboot.entity.Record;
import com.example.springboot.entity.RecordRes;
import com.example.springboot.service.IProductService;
import com.example.springboot.service.IRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RecordControllerTests {

    @Mock
    private IRecordService recordService;

    @Mock
    private IProductService productService;

    @InjectMocks
    private RecordController recordController;

    private Record record;
    private Product product;

    @BeforeEach
    public void setUp() {
        record = new Record();
        record.setId(1);
        record.setProduct("1");
        record.setCount(10);
        record.setAction("1");
        record.setCreateTime(LocalDateTime.now());

        product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setCount(100);
        product.setCategory("Test Category");
    }

    @Test
    public void testSaveRecord() {
        // Arrange
        when(productService.getById("1")).thenReturn(product);
        when(recordService.saveOrUpdate(record)).thenReturn(true);
        when(productService.updateById(product)).thenReturn(true);

        // Act
        Result result = recordController.save(record);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());  // Assuming Result.success() returns code 200
        //assertEquals("success", result.getMessage());
        verify(productService, times(1)).getById("1");
        verify(productService, times(1)).updateById(product);
        verify(recordService, times(1)).saveOrUpdate(record);
    }

    @Test
    public void testFindAll() {
        // Arrange
        List<Record> recordList = Arrays.asList(record);
        when(recordService.list()).thenReturn(recordList);

        // Act
        Result result = recordController.findAll();

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());
        //assertEquals("success", result.getMessage());
        assertEquals(1, ((List<?>) result.getData()).size());
        assertEquals(10, ((Record) ((List<?>) result.getData()).get(0)).getCount());
        verify(recordService, times(1)).list();
    }

    @Test
    public void testFindOne() {
        // Arrange
        when(recordService.getById(1)).thenReturn(record);

        // Act
        Result result = recordController.findOne(1);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());
        //assertEquals("success", result.getMessage());
        assertEquals(10, ((Record) result.getData()).getCount());
        verify(recordService, times(1)).getById(1);
    }

    @Test
    public void testFindPage() {
        // Arrange
        List<RecordRes> recordResList = Arrays.asList(new RecordRes());
        Page<RecordRes> recordPage = new Page<>(1, 10);
        recordPage.setRecords(recordResList);

        when(recordService.findRecordPage(any(), any())).thenReturn(recordPage);

        // Act
        Result result = recordController.findPage("Test", "Test Category", 1, 10);

        // Assert
        assertNotNull(result);
        //assertEquals(200, result.getCode());
        //assertEquals("success", result.getMessage());
        assertEquals(1, ((Page<?>) result.getData()).getRecords().size());
        //assertEquals("Test Product", ((RecordRes) ((Page<?>) result.getData()).getRecords().get(0)).getProduct().getName());
        verify(recordService, times(1)).findRecordPage(any(), any());
    }
}
