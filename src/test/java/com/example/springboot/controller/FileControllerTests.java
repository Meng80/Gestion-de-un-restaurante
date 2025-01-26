package com.example.springboot.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Files;
import com.example.springboot.mapper.FileMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FileControllerTests {

    @InjectMocks
    private FileController fileController;

    @Mock
    private FileMapper fileMapper;

    @Mock
    private FileReader mockFileReader; // Mock FileReader

    private String fileUploadPath = "testUploads/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileController.fileUploadPath = fileUploadPath;
        // Ensure the upload directory exists for testing
        new File(fileUploadPath).mkdirs();
    }


    @Test
    void testUploadExistingFile() throws IOException {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        Files mockDbFile = new Files();
        mockDbFile.setUrl("http://localhost:9090/file/existingFile.txt");

        when(fileMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(mockDbFile));

        // Act
        String url = fileController.upload(mockFile);

        // Assert
        assertEquals(mockDbFile.getUrl(), url);
    }

    @Test
    public void testUpdate() {
        // Arrange
        Files file = new Files();
        file.setId(1);
        file.setName("updated.txt");
        when(fileMapper.updateById(any(Files.class))).thenReturn(1);

        // Act
        Result result = fileController.update(file);

        // Assert
        assertEquals(Result.success(1), result);
    }

    @Test
    void testDeleteFile() {
        // Arrange
        Files mockFile = new Files();
        mockFile.setId(1);
        mockFile.setIsDelete(false);

        when(fileMapper.selectById(1)).thenReturn(mockFile);
        when(fileMapper.updateById(mockFile)).thenReturn(1);

        // Act
        Result result = fileController.delete(1);

        // Assert
        assertEquals(Result.success(), result);
        verify(fileMapper, times(1)).updateById(mockFile);
    }

    @Test
    public void testDeleteBatch() {
        // Arrange
        Files file1 = new Files();
        file1.setId(1);
        Files file2 = new Files();
        file2.setId(2);
        when(fileMapper.selectList(any())).thenReturn(Arrays.asList(file1, file2));

        // Act
        Result result = fileController.deleteBatch(Arrays.asList(1, 2));

        // Assert
        assertEquals(result.success(), result);
        verify(fileMapper, times(2)).updateById(any(Files.class));
    }

    @Test
    void testFindPage() {
        // Arrange
        Page<Files> mockPage = new Page<>();
        when(fileMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        Result result = fileController.findPage(1, 10, "test");

        // Assert
        assertEquals(Result.success(mockPage), result);
        verify(fileMapper, times(1)).selectPage(any(Page.class), any(QueryWrapper.class));
    }
}
