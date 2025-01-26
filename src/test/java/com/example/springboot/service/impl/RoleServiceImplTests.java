package com.example.springboot.service.impl;

import com.example.springboot.entity.Menu;
import com.example.springboot.entity.RoleMenu;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.service.impl.RoleServiceImpl;
import com.example.springboot.service.IMenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleServiceImplTests {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleMenuMapper roleMenuMapper;

    @Mock
    private IMenuService menuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
    }

    @Test
    void testSetRoleMenu_withValidData() {
        Integer roleId = 1;
        List<Integer> menuIds = Arrays.asList(1, 2, 3);

        // Mocking Menu service behavior
        Menu menu1 = new Menu();
        menu1.setPid(null); // top-level menu
        when(menuService.getById(1)).thenReturn(menu1);

        Menu menu2 = new Menu();
        menu2.setPid(1); // second-level menu
        when(menuService.getById(2)).thenReturn(menu2);

        Menu menu3 = new Menu();
        menu3.setPid(2); // second-level menu
        when(menuService.getById(3)).thenReturn(menu3);

        // Call the service method
        roleService.setRoleMenu(roleId, menuIds);

        // Verify that the methods in roleMenuMapper are called
        verify(roleMenuMapper).deleteByRoleId(roleId); // Ensure delete by roleId is called

        // Verify insert operations
        verify(roleMenuMapper, times(3)).insert(any(RoleMenu.class)); // Ensure insert is called for each menu
    }

    @Test
    void testSetRoleMenu_withEmptyMenuIds() {
        Integer roleId = 1;
        List<Integer> menuIds = Arrays.asList();  // Empty menuIds

        roleService.setRoleMenu(roleId, menuIds);

        // Verify that no insertion occurs since the list is empty
        verify(roleMenuMapper, times(0)).insert(any(RoleMenu.class));
    }

    @Test
    void testGetRoleMenu_withValidRoleId() {
        Integer roleId = 1;
        List<Integer> expectedMenuIds = Arrays.asList(1, 2, 3);

        // Mocking roleMenuMapper behavior
        when(roleMenuMapper.selectByRoleId(roleId)).thenReturn(expectedMenuIds);

        // Call the service method
        List<Integer> actualMenuIds = roleService.getRoleMenu(roleId);

        // Assert that the actual returned menuIds match the expected ones
        assertEquals(expectedMenuIds, actualMenuIds);
    }

    @Test
    void testGetRoleMenu_withInvalidRoleId() {
        Integer roleId = 999;  // Non-existent roleId
        List<Integer> expectedMenuIds = Arrays.asList();

        // Mocking roleMenuMapper behavior
        when(roleMenuMapper.selectByRoleId(roleId)).thenReturn(expectedMenuIds);

        // Call the service method
        List<Integer> actualMenuIds = roleService.getRoleMenu(roleId);

        // Assert that the actual returned menuIds is empty
        assertTrue(actualMenuIds.isEmpty());
    }
}
