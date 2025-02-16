package com.example.springboot.service.impl;

import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IMenuService;
import com.example.springboot.utils.TokenUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RoleMenuMapper roleMenuMapper;

    @Mock
    private IMenuService menuService;

    @Mock
    private JavaMailSender javaMailSender;

    private UserDTO userDTO;
    private UserPasswordDTO userPasswordDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");

        userPasswordDTO = new UserPasswordDTO();
        userPasswordDTO.setUsername("testuser");
        userPasswordDTO.setNewPassword("newPassword123");
    }

    //@Test
    public void testLogin_validUser_returnsToken() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
        mockUser.setRole("ADMIN");

        //when(userMapper.getOne(any())).thenReturn(mockUser); // Mock the user query

        // Act
        UserDTO result = userService.login(userDTO);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertNotNull(result.getToken());  // Check if a token is generated
    }

    @Test
    public void testLogin_invalidUser_throwsServiceException() {
        // Arrange
        //when(userMapper.getOne(any())).thenReturn(null); // Simulate user not found

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.login(userDTO);
        });
        assertEquals("Error System", exception.getMessage());
    }

    //@Test
    public void testRegister_userNotExist_createsNewUser() {
        // Arrange
        UserDTO newUserDTO = new UserDTO();
        newUserDTO.setUsername("newuser");
        newUserDTO.setPassword("newpassword");

        //when(userMapper.getOne(any())).thenReturn(null);  // Simulate no existing user

        // Act
        User result = userService.register(newUserDTO);

        // Assert
        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        //verify(userMapper, times(1)).save(any());  // Ensure save was called
    }

    @Test
    public void testRegister_userAlreadyExists_throwsServiceException() {
        // Arrange
        //when(userMapper.getOne(any())).thenReturn(new User());  // Simulate user exists

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.register(userDTO);
        });
        assertEquals("Error System", exception.getMessage());
    }

    @Test
    public void testSendEmailCode_success() {
        // Arrange
        String email = "test@example.com";
        SimpleMailMessage mockMessage = mock(SimpleMailMessage.class);

        // Act
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));  // Mock send email method
        userService.sendEmailCode(email);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));  // Verify email was sent
    }

    @Test
    public void testSendEmailCode_failure_throwsServiceException() {
        // Arrange
        String email = "test@example.com";
        doThrow(new RuntimeException("Email sending failed")).when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.sendEmailCode(email);
        });
        assertEquals("Failed to send verification code", exception.getMessage());
    }

    @Test
    public void testUpdatePassword_success() {
        // Arrange
        when(userMapper.updatePassword(any(UserPasswordDTO.class))).thenReturn(1);  // Mock password update

        // Act
        userService.updatePassword(userPasswordDTO);

        // Assert
        verify(userMapper, times(1)).updatePassword(any(UserPasswordDTO.class));  // Verify method was called
    }

    @Test
    public void testUpdatePassword_failure_throwsServiceException() {
        // Arrange
        when(userMapper.updatePassword(any(UserPasswordDTO.class))).thenReturn(0);  // Mock failed password update

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.updatePassword(userPasswordDTO);
        });
        assertEquals("Password error", exception.getMessage());
    }
}
