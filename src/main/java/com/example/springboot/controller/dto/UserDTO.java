package com.example.springboot.controller.dto;
import com.example.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * Accept parameters for frontend login requests
 */
@Data
public class UserDTO {
        private String username;
        private String password;
        private String nickname;
        private String avatarUrl;
        private String token;
        private String role;
        private List<Menu> menus;
}

