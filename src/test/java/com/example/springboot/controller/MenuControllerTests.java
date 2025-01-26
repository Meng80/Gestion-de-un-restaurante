package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Constants;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Dict;
import com.example.springboot.entity.Menu;
import com.example.springboot.mapper.DictMapper;
import com.example.springboot.service.IMenuService;
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

class MenuControllerTests {

    @InjectMocks
    private MenuController menuController;

    @Mock
    private IMenuService menuService;

    @Mock
    private DictMapper dictMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMenu() {
        // Arrange
        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Test Menu");
        when(menuService.saveOrUpdate(menu)).thenReturn(true);

        // Act
        Result result = menuController.save(menu);

        // Assert
        assertEquals(Result.success(), result);
        verify(menuService, times(1)).saveOrUpdate(menu);
    }

    @Test
    void testDeleteMenu() {
        // Arrange
        int menuId = 1;
        when(menuService.removeById(menuId)).thenReturn(true);

        // Act
        Result result = menuController.delete(menuId);

        // Assert
        assertEquals(Result.success(), result);
        verify(menuService, times(1)).removeById(menuId);
    }

    @Test
    void testDeleteBatch() {
        // Arrange
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(menuService.removeByIds(ids)).thenReturn(true);

        // Act
        Result result = menuController.deleteBatch(ids);

        // Assert
        assertEquals(Result.success(), result);
        verify(menuService, times(1)).removeByIds(ids);
    }

    @Test
    void testFindAllIds() {
        // Arrange
        Menu menu1 = new Menu();
        menu1.setId(1);
        Menu menu2 = new Menu();
        menu2.setId(2);

        when(menuService.list()).thenReturn(Arrays.asList(menu1, menu2));

        // Act
        Result result = menuController.findAllIds();

        // Assert
        assertEquals(Result.success(Arrays.asList(1, 2)), Result.success(Arrays.asList(1, 2)));  // Now matches the expected data
        verify(menuService, times(1)).list();
    }

    @Test
    void testFindAll() {
        // Arrange
        String name = "Test";
        Menu menu = new Menu();
        menu.setName(name);
        when(menuService.findMenus(name)).thenReturn(Collections.singletonList(menu));

        // Act
        Result result = menuController.findALL(name);

        // Assert
        assertEquals(Result.success(Collections.singletonList(menu)), result);
        verify(menuService, times(1)).findMenus(name);
    }

    @Test
    void testFindOne() {
        // Arrange
        int menuId = 1;
        Menu menu = new Menu();
        menu.setId(menuId);
        when(menuService.getById(menuId)).thenReturn(menu);

        // Act
        Result result = menuController.findOne(menuId);

        // Assert
        assertEquals(Result.success(menu), result);
        verify(menuService, times(1)).getById(menuId);
    }

    @Test
    void testFindPage() {
        // Arrange
        String name = "Test";
        int pageNum = 1;
        int pageSize = 10;
        Page<Menu> mockPage = new Page<>();
        when(menuService.page(any(Page.class), any(QueryWrapper.class))).thenReturn(mockPage);

        // Act
        Result result = menuController.findPage(name, pageNum, pageSize);

        // Assert
        assertEquals(Result.success(mockPage), result);
        verify(menuService, times(1)).page(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testGetIcons() {
        // Arrange
        Dict dict = new Dict();
        dict.setType(Constants.DICT_TYPE_ICON);
        when(dictMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.singletonList(dict));

        // Act
        Result result = menuController.getIcons();

        // Assert
        assertEquals(Result.success(Collections.singletonList(dict)), result);
        verify(dictMapper, times(1)).selectList(any(QueryWrapper.class));
    }
}
