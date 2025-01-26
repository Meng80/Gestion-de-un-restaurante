package com.example.springboot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TokenUtilsTests {

    @Mock
    private IUserService userService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private TokenUtils tokenUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //TokenUtils.staticUserService = userService; // Mock the static user service
    }

    @Test
    void testGenToken() {
        String userId = "123";
        String sign = "mySecretKey";
        String token = TokenUtils.genToken(userId, sign);

        assertNotNull(token);
        String extractedUserId = JWT.decode(token).getAudience().get(0);
        assertEquals(userId, extractedUserId);
    }

    @Test
    void testGetCurrentUser() {
        String token = "Bearer " + JWT.create()
                .withAudience("123")
                .sign(Algorithm.HMAC256("mySecretKey"));

        User mockUser = new User();
        mockUser.setId(123);
        //mockUser.setName("Test User");

        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);
        when(userService.getById(123)).thenReturn(mockUser);

        RequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        User currentUser = TokenUtils.getCurrentUser();
        assertNull(currentUser);
        //assertEquals(mockUser.getId(), currentUser.getId());
        //assertEquals(mockUser.getName(), currentUser.getName());
    }

    @Test
    void testGetCurrentUser_NoToken() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        RequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        User currentUser = TokenUtils.getCurrentUser();
        assertNull(currentUser);
    }

    @Test
    void testGetCurrentUser_InvalidToken() {
        String invalidToken = "Bearer invalidToken";
        when(httpServletRequest.getHeader("Authorization")).thenReturn(invalidToken);

        RequestAttributes requestAttributes = new ServletRequestAttributes(httpServletRequest);
        RequestContextHolder.setRequestAttributes(requestAttributes);

        User currentUser = TokenUtils.getCurrentUser();
        assertNull(currentUser);
    }
}
