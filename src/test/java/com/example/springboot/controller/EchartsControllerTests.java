package com.example.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Vip;
import com.example.springboot.service.IVipService;
import com.example.springboot.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EchartsControllerTests {

    @Mock
    private IVipService vipService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private EchartsController echartsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalEmployees() {
        // Arrange
        when(userService.count()).thenReturn(100L);

        // Act
        Result result = echartsController.getTotalEmployees();

        // Assert
        assertEquals(Result.success(100), result);
        verify(userService, times(1)).count();
    }

    @Test
    void testGetTotalVIPCustomers() {
        // Arrange
        when(vipService.count()).thenReturn(50L);

        // Act
        Result result = echartsController.getTotalVIPCustomers();

        // Assert
        assertEquals(Result.success(50), result);
        verify(vipService, times(1)).count();
    }

    @Test
    void testMembers() {
        // Arrange
        Date dateQ1 = new Date(1230768000000L); // Jan 1, 2009
        Date dateQ2 = new Date(1246406400000L); // Jul 1, 2009
        Date dateQ3 = new Date(1257033600000L); // Oct 1, 2009
        Date dateQ4 = new Date(1262304000000L); // Dec 31, 2009

        Vip vip1 = new Vip();
        vip1.setCreateTime(dateQ1);
        Vip vip2 = new Vip();
        vip2.setCreateTime(dateQ2);
        Vip vip3 = new Vip();
        vip3.setCreateTime(dateQ3);
        Vip vip4 = new Vip();
        vip4.setCreateTime(dateQ4);

        List<Vip> vipList = Arrays.asList(vip1, vip2, vip3, vip4);
        when(vipService.list()).thenReturn(vipList);

        // Act
        Result result = echartsController.members();

        // Assert
        assertEquals(Result.success(CollUtil.newArrayList(2, 0, 1, 1)), result);
        verify(vipService, times(1)).list();
    }
}
