package com.example.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.example.springboot.common.Constants;
import com.example.springboot.controller.dto.UserDTO;
import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.Menu;
import com.example.springboot.entity.User;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.RoleMapper;
import com.example.springboot.mapper.RoleMenuMapper;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.service.IMenuService;
import com.example.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String from;
    private static final Log LOG = Log.get();
    @Resource private UserMapper userMapper;
    @Resource private RoleMapper roleMapper;
    @Resource private RoleMenuMapper roleMenuMapper;
    @Resource private IMenuService menuService;
    @Override
    public UserDTO login(UserDTO userDTO) {
        User one = getUserInfo(userDTO);if (one != null) {
            BeanUtil.copyProperties(one, userDTO, true);String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());userDTO.setToken(token);String role = one.getRole();List<Menu> roleMenus = getRoleMenus(role);userDTO.setMenus(roleMenus);return userDTO;
        } else {throw new ServiceException(Constants.CODE_600, "Error username or password"); }
    }
    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);if(one == null){one = new User();BeanUtil.copyProperties(userDTO, one,true);save(one);
        }else{throw new ServiceException(Constants.CODE_600, "Ya exist user"); }return one;
    }
    @Override
    public void sendEmailCode(String email) {
        String code = String.valueOf((int)((Math.random() * 9 + 1) * 1000));log.info("Generated verification code: {}", code);SimpleMailMessage message = new SimpleMailMessage();message.setFrom(from);message.setTo(email);message.setSubject("Registration Verification Code");message.setText("Your verification code is: " + code + ". It is valid for 5 minutes.");
        try {javaMailSender.send(message);log.info("Verification code sent to email: {}", email);
        } catch (Exception e) {log.error("Error sending email: {}", e.getMessage());throw new ServiceException(Constants.CODE_500, "Failed to send verification code"); }
    }
    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        int update = userMapper.updatePassword(userPasswordDTO);
        if (update < 1) {throw new ServiceException(Constants.CODE_600, "Password error"); }
    }
    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();queryWrapper.eq("username", userDTO.getUsername());queryWrapper.eq("password", userDTO.getPassword());User one;
        try {one = this.getOne(queryWrapper); } catch (Exception e) {LOG.error(e);throw new ServiceException(Constants.CODE_500, "Error System"); }return one;
    }
    List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);List<Menu> menus = menuService.findMenus("");List<Menu> roleMenus = new ArrayList<>();
        for (Menu menu : menus) {if(menuIds.contains(menu.getId())) { roleMenus.add(menu);
            }List<Menu> children = menu.getChildren();children.removeIf(child -> !menuIds.contains(child.getId()));
        }return roleMenus;
    }
}
