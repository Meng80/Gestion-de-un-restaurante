package com.example.springboot.service;

import com.example.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  Service
 * </p>
 *
 * @author ada
 * @since 2023-06-22
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
