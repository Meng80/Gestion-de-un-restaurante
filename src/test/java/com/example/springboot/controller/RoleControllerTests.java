package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Role;
import com.example.springboot.service.IRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoleControllerTests {

    @InjectMocks
    private RoleController roleController;

    @Mock
    private IRoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRole() {
        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("Test Role");
        when(roleService.saveOrUpdate(role)).thenReturn(true);

        // Act
        Result result = roleController.save(role);

        // Assert
        assertEquals(Result.success(), result);
        verify(roleService, times(1)).saveOrUpdate(role);
    }

    @Test
    void testDeleteRole() {
        // Arrange
        int roleId = 1;
        when(roleService.removeById(roleId)).thenReturn(true);

        // Act
        Result result = roleController.delete(roleId);

        // Assert
        assertEquals(Result.success(), result);
        verify(roleService, times(1)).removeById(roleId);
    }

    @Test
    void testDeleteBatchRoles() {
        // Arrange
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(roleService.removeByIds(ids)).thenReturn(true);

        // Act
        Result result = roleController.deleteBatch(ids);

        // Assert
        assertEquals(Result.success(), result);
        verify(roleService, times(1)).removeByIds(ids);
    }

    @Test
    void testFindAllRoles() {
        // Arrange
        Role role = new Role();
        role.setName("Test Role");
        when(roleService.list()).thenReturn(Collections.singletonList(role));

        // Act
        Result result = roleController.findALL();

        // Assert
        assertEquals(Result.success(Collections.singletonList(role)), result);
        verify(roleService, times(1)).list();
    }

    @Test
    void testFindOneRole() {
        // Arrange
        int roleId = 1;
        Role role = new Role();
        role.setId(roleId);
        when(roleService.getById(roleId)).thenReturn(role);

        // Act
        Result result = roleController.findOne(roleId);

        // Assert
        assertEquals(Result.success(role), result);
        verify(roleService, times(1)).getById(roleId);
    }

    @Test
    void testFindPageRoles() {
        // Arrange
        String name = "Test Role";
        int pageNum = 1;
        int pageSize = 10;
        Page<Role> mockPage = new Page<>();
        when(roleService.page(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        Result result = roleController.findPage(name, pageNum, pageSize);

        // Assert
        assertEquals(Result.success(mockPage), result);
        verify(roleService, times(1)).page(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testRoleMenu() {
        // Arrange
        int roleId = 1;
        List<Integer> menuIds = Arrays.asList(1, 2, 3);
        doNothing().when(roleService).setRoleMenu(roleId, menuIds);

        // Act
        Result result = roleController.roleMenu(roleId, menuIds);

        // Assert
        assertEquals(Result.success(), result);
        verify(roleService, times(1)).setRoleMenu(roleId, menuIds);
    }

    @Test
    void testGetRoleMenu() {
        // Arrange
        int roleId = 1;
        List<Integer> menuIds = Arrays.asList(1, 2, 3);
        when(roleService.getRoleMenu(roleId)).thenReturn(menuIds);

        // Act
        Result result = roleController.getRoleMenu(roleId);

        // Assert
        assertEquals(Result.success(menuIds), result);
        verify(roleService, times(1)).getRoleMenu(roleId);
    }
}
