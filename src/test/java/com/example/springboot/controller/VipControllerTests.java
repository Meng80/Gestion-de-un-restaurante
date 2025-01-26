package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Vip;
import com.example.springboot.service.IVipService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VipControllerTests {

    @InjectMocks
    private VipController vipController;

    @Mock
    private IVipService vipService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ServletOutputStream outputStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Vip vip = new Vip();
        vip.setId(1);
        vip.setName("Test Vip");

        // Simulate the behavior of saveOrUpdate
        when(vipService.saveOrUpdate(vip)).thenReturn(true);

        Result result = vipController.save(vip);

        // Assertions and verifications
        assertEquals(Result.success(), result);
        verify(vipService, times(1)).saveOrUpdate(vip);
    }


    @Test
    void testDelete() {
        int id = 1;

        // Correct for non-void methods
        when(vipService.removeById(id)).thenReturn(true); // Replace true with the actual expected return value

        Result result = vipController.delete(id);

        assertEquals(Result.success(), result);
        verify(vipService, times(1)).removeById(id);
    }

    @Test
    void testDeleteBatch() {
        // Arrange
        List<Integer> ids = Arrays.asList(1, 2, 3);

        // Mock the method to return a value (e.g., true if successful)
        when(vipService.removeByIds(ids)).thenReturn(true); // Adjust based on the actual return type

        // Act
        Result result = vipController.deleteBatch(ids);

        // Assert
        assertEquals(Result.success(), result);
        verify(vipService, times(1)).removeByIds(ids);
    }


    @Test
    void testFindAll() {
        Vip vip = new Vip();
        vip.setName("Test Vip");

        when(vipService.list()).thenReturn(Collections.singletonList(vip));

        Result result = vipController.findALL();

        assertEquals(Result.success(Collections.singletonList(vip)), result);
        verify(vipService, times(1)).list();
    }

    @Test
    void testFindOne() {
        int id = 1;
        Vip vip = new Vip();
        vip.setId(id);

        when(vipService.getById(id)).thenReturn(vip);

        Result result = vipController.findOne(id);

        assertEquals(Result.success(vip), result);
        verify(vipService, times(1)).getById(id);
    }

    @Test
    void testFindPage() {
        String name = "Test";
        int pageNum = 1;
        int pageSize = 10;

        Page<Vip> mockPage = new Page<>();
        when(vipService.page(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        Result result = vipController.findPage(name, pageNum, pageSize, "", "");

        assertEquals(Result.success(mockPage), result);
        verify(vipService, times(1)).page(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testExport() throws Exception {
        // Arrange: Create a sample Vip object
        Vip vip = new Vip();
        vip.setName("Test Vip");

        // Mock the list response from the service
        when(vipService.list()).thenReturn(Collections.singletonList(vip));

        // Mock the HttpServletResponse to avoid writing to a file
        when(response.getOutputStream()).thenReturn(outputStream);

        // Act: Call the export method
        vipController.export(response);

        // Assert: Verify the service method was called and output stream was flushed
        verify(vipService, times(1)).list();
        verify(response, times(1)).getOutputStream(); // Ensure getOutputStream was called

        // Adjust the expected number of flush calls based on your logic
        verify(outputStream, atLeastOnce()).flush(); // If flush could be called multiple times, use atLeastOnce() or a specific number
    }

    @Test
    void testImport() throws Exception {
        // Create a new workbook and sheet using Apache POI
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Create a row and add some data
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Name");
        row.createCell(1).setCellValue("Age");

        row = sheet.createRow(1);
        row.createCell(0).setCellValue("John Doe");
        row.createCell(1).setCellValue(30);

        // Write the workbook to ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        // Create MockMultipartFile from the byte array
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                baos.toByteArray()  // Use the byte array generated from POI
        );

        // Mock the saveBatch method to avoid actual DB operation
        when(vipService.saveBatch(anyList())).thenReturn(true);

        // Act: Call the import method with the non-empty file
        Result result = vipController.imp(file);

        // Assert: Verify that saveBatch was called exactly once and check the result
        assertEquals(Result.success(true), result);
        verify(vipService, times(1)).saveBatch(anyList()); // Ensure saveBatch was called once
    }

    @Test
    void testGetRecentVisitorsLastMonth() {
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1);

        when(vipService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        Result result = vipController.getRecentVisitors("lastMonth");

        assertEquals(Result.success(Collections.emptyList()), result);
        verify(vipService, times(1)).list(any(QueryWrapper.class));
    }

    @Test
    void testGetRecentVisitorsLongTimeNoVisit() {
        LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6);

        when(vipService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        Result result = vipController.getRecentVisitors("longTimeNoVisit");

        assertEquals(Result.success(Collections.emptyList()), result);
        verify(vipService, times(1)).list(any(QueryWrapper.class));
    }
}
