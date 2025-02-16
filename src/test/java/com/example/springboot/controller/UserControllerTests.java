package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import com.example.springboot.utils.TokenUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private IUserService userService;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("test");
        userDTO.setPassword("123456");
        when(userService.login(userDTO)).thenReturn(userDTO);

        // Act
        Result result = userController.login(userDTO);

        // Assert
        assertEquals(Result.success(userDTO), result);
        verify(userService, times(1)).login(userDTO);
    }

    @Test
    void testRegister_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");
        userDTO.setPassword("securePass");
        userDTO.setEmail("user@example.com");
        userDTO.setCode("1234");

        when(userService.register(any(UserDTO.class))).thenReturn(null);

        Result result = userController.register(userDTO);
        assertEquals(Result.success(), result);
        assertEquals(null, result.getData());
    }

    @Test
    void testSaveUser() {
        // Arrange
        User user = new User();
        user.setId(1);
        when(userService.saveOrUpdate(user)).thenReturn(true);

        // Act
        Result result = userController.save(user);

        // Assert
        assertEquals(Result.success(true), result);
        verify(userService, times(1)).saveOrUpdate(user);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        int userId = 1;
        when(userService.removeById(userId)).thenReturn(true);

        // Act
        Result result = userController.delete(userId);

        // Assert
        assertEquals(Result.success(true), result);
        verify(userService, times(1)).removeById(userId);
    }

    @Test
    void testDeleteBatchUsers() {
        // Arrange
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(userService.removeByIds(ids)).thenReturn(true);

        // Act
        Result result = userController.deleteBatch(ids);

        // Assert
        assertEquals(Result.success(true), result);
        verify(userService, times(2)).removeByIds(ids);
    }

    @Test
    void testFindAllUsers() {
        // Arrange
        User user = new User();
        user.setUsername("test");
        when(userService.list()).thenReturn(Collections.singletonList(user));

        // Act
        Result result = userController.findALL();

        // Assert
        assertEquals(Result.success(Collections.singletonList(user)), result);
        verify(userService, times(1)).list();
    }

    @Test
    void testFindOneUser() {
        // Arrange
        String username = "test";
        User user = new User();
        user.setUsername(username);
        when(userService.getOne(any(QueryWrapper.class))).thenReturn(user);

        // Act
        Result result = userController.findOne(username);

        // Assert
        assertEquals(Result.success(user), result);
        verify(userService, times(1)).getOne(any(QueryWrapper.class));
    }

    @Test
    void testFindPageUsers() {
        // Arrange
        String username = "test";
        int pageNum = 1;
        int pageSize = 10;
        Page<User> mockPage = new Page<>();
        when(userService.page(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        Result result = userController.findPage(pageNum, pageSize, username, "", "");

        // Assert
        assertEquals(Result.success(mockPage), result);
        verify(userService, times(1)).page(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testExport() throws Exception {
        // Mocking user data
        List<User> mockUsers = Arrays.asList(
                new User(),
                new User()
        );
        when(userService.list()).thenReturn(mockUsers);

        // Mocking HttpServletResponse
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Call the method under test
        userController.export(response);

        // Validate response content type
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8", response.getContentType());

        // Validate response headers
        String expectedHeader = "attachment;filename=UserInformation.xlsx";
        assertEquals(expectedHeader, response.getHeader("Content-Disposition"));

        // Validate response content (optional)
        byte[] outputContent = response.getContentAsByteArray();
        assertNotNull(outputContent);
        assertTrue(outputContent.length > 0);

        // Verify interactions with mocked services
        verify(userService, times(1)).list();
    }

    @Test
    void testImportUsers() throws Exception {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Header1");
        workbook.write(outputStream);
        workbook.close();

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new ByteArrayInputStream(outputStream.toByteArray())
        );
        when(userService.saveBatch(anyList())).thenReturn(true);

        // Act
        Result result = userController.imp(file);

        // Assert
        assertEquals(Result.success(true), result);
        verify(userService, times(1)).saveBatch(anyList());
    }


    @Test
    void testChangePassword() {
        // Arrange
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        //userPasswordDTO.setOldPassword("123");
        userPasswordDTO.setNewPassword("456");
        doNothing().when(userService).updatePassword(userPasswordDTO);

        // Act
        Result result = userController.password(userPasswordDTO);

        // Assert
        assertEquals(Result.success(), result);
        verify(userService, times(1)).updatePassword(userPasswordDTO);
    }
}
