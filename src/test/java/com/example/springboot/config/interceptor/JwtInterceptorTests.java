package com.example.springboot.config.interceptor;

import com.example.springboot.common.Constants;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JwtInterceptorTests {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HandlerMethod handler;

    @InjectMocks
    private JwtInterceptor jwtInterceptor;

    private static final String VALID_TOKEN = "valid.token.string";
    private static final String INVALID_TOKEN = "invalid.token.string";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPreHandle_NoAuthorizationHeader() {
        // Simulate request without Authorization header
        when(request.getHeader("Authorization")).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            jwtInterceptor.preHandle(request, response, handler);
        });

        assertEquals(Constants.CODE_401, exception.getCode());
        assertEquals("no token, try again", exception.getMessage());
    }

    @Test
    void testPreHandle_InvalidTokenFormat() {
        // Simulate request with incorrect token format
        when(request.getHeader("Authorization")).thenReturn("BearerInvalid");

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            jwtInterceptor.preHandle(request, response, handler);
        });

        assertEquals(Constants.CODE_401, exception.getCode());
        assertEquals("no token, try again", exception.getMessage());
    }

    @Test
    void testPreHandle_TokenNotDecoded() {
        // Simulate request with a valid token, but JWT decode error
        when(request.getHeader("Authorization")).thenReturn("Bearer " + INVALID_TOKEN);
        //when(handler instanceof HandlerMethod).thenReturn(true);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            jwtInterceptor.preHandle(request, response, handler);
        });

        assertEquals(Constants.CODE_400, exception.getCode());
        assertEquals("token error，try again", exception.getMessage());
    }

    @Test
    void testPreHandle_UserNotExist() {
        // Simulate a valid token and user ID, but the user does not exist in the database
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        when(userService.getById(anyString())).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            jwtInterceptor.preHandle(request, response, handler);
        });

        assertEquals(Constants.CODE_400, exception.getCode());
        assertEquals("token error，try again", exception.getMessage());
    }

    @Test
    void testPreHandle_TokenVerificationFailed() {
        // Simulate a valid token but failed verification
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        User mockUser = new User();
        mockUser.setPassword("wrong_password");
        when(userService.getById(anyString())).thenReturn(mockUser);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            jwtInterceptor.preHandle(request, response, handler);
        });

        assertEquals(Constants.CODE_400, exception.getCode());
        assertEquals("token error，try again", exception.getMessage());
    }

    //@Test
    void testPreHandle_Success() {
        // Simulate a valid token and successful verification
        when(request.getHeader("Authorization")).thenReturn("Bearer " + VALID_TOKEN);
        User mockUser = new User();
        mockUser.setPassword("valid_password");
        when(userService.getById(anyString())).thenReturn(mockUser);

        // Assuming handler is an instance of HandlerMethod
        //when(handler instanceof HandlerMethod).thenReturn(true);

        assertTrue(jwtInterceptor.preHandle(request, response, handler));
    }
}
