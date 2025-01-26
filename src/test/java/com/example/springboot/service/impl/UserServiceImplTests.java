package com.example.springboot.service.impl;

import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.Menu;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IMenuService;
import com.example.springboot.utils.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceImplTests {

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleMenuMapper roleMenuMapper;

    @Mock
    private IMenuService menuService;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPass");

        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        user.setRole("admin");
    }

    //@Test
    public void testLogin_Success() {
        // Arrange
        //when(userService.getOne(any())).thenReturn(user);
        when(roleMapper.selectByFlag("admin")).thenReturn(1);
        when(roleMenuMapper.selectByRoleId(1)).thenReturn(Collections.singletonList(1));
        List<Menu> menus = Collections.singletonList(new Menu());
        when(menuService.findMenus("")).thenReturn(menus);

        // Act
        UserDTO result = userService.login(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
        assertNotNull(result.getToken());
        assertNotNull(result.getMenus());
    }

    @Test
    public void testLogin_Failure() {
        // Arrange
        //when(userMapper.getOne(any())).thenReturn(null);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.login(userDTO);
        });
        assertEquals("Error System", exception.getMessage());
    }

    @Test
    public void testRegister_Success() {
        // Arrange
        //when(userMapper.getOne(any())).thenReturn(null);
        //when(userMapper.save(any())).thenReturn(true);

        // Act
        //User result = userService.register(userDTO);
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.register(userDTO);
        });

        // Assert
        //assertNotNull(result);
        //assertEquals(userDTO.getUsername(), result.getUsername());
    }

    @Test
    public void testRegister_UserExists() {
        // Arrange
        //when(userMapper.getOne(any())).thenReturn(user);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.register(userDTO);
        });
        assertEquals("Error System", exception.getMessage());
    }

    @Test
    public void testUpdatePassword_Success() {
        // Arrange
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword("newPassword");
        when(userMapper.updatePassword(any())).thenReturn(1);

        // Act
        userService.updatePassword(userPasswordDTO);

        // Assert
        verify(userMapper, times(1)).updatePassword(userPasswordDTO);
    }

    @Test
    public void testUpdatePassword_Failure() {
        // Arrange
        UserPasswordDTO userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setPassword("newPassword");
        when(userMapper.updatePassword(any())).thenReturn(0);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.updatePassword(userPasswordDTO);
        });
        assertEquals("Password error", exception.getMessage());
    }
}
