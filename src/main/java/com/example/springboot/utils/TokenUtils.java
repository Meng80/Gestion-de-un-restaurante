package com.example.springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springboot.entity.User;
import com.example.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {
    private static IUserService staticUserService;

    @Autowired
    private IUserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }
    /**
     * Generate token
     *
     * @return
     */
    public static String genToken(String userId, String sign){
        return  JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * Get information of the currently logged-in user
     *
     * @return user object
     */
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization");
            if (StrUtil.isNotBlank(token) && token.startsWith("Bearer ")) {
                token = token.substring(7);
            } else {
                token = request.getHeader("token");
            }
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
//                System.out.println("Token: " + token);
//                System.out.println("UserId from Token: " + userId);
                User user = staticUserService.getById(Integer.valueOf(userId));
//                System.out.println("User Retrieved: " + user);
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
