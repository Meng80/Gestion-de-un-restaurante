package com.example.springboot.config.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springboot.common.Constants;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserServiceImpl userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StrUtil.isBlank(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) { throw new ServiceException(Constants.CODE_401, "no token, try again");
        }String token = authorizationHeader.substring(7);
        if(!(handler instanceof HandlerMethod)){return true;
        } if (StrUtil.isBlank(token) ) {throw new ServiceException(Constants.CODE_401, "no token，try again");
        }String userId;try {userId = JWT.decode(token).getAudience().get(0); } catch (JWTDecodeException j) { throw new ServiceException(Constants.CODE_400, "token error，try again");
        }User user = userService.getById(userId);if (user == null) { throw new ServiceException(Constants.CODE_404, "user no exist. try again");
        }JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {jwtVerifier.verify(token); } catch (JWTVerificationException e) {throw new ServiceException(Constants.CODE_403, "token verification failed，try again"); }return true;
    }
}
