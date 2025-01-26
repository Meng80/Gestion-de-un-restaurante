package com.example.springboot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import io.swagger.annotations.Api;



import com.example.springboot.service.IRoleService;
import com.example.springboot.entity.Role;

//import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  RoleController
 * </p>
 *
 * @author ada
 * @since 2023-06-25
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    
    @Resource
    private IRoleService roleService;

    @PutMapping
    public Result save(@RequestBody Role role){
       roleService.saveOrUpdate(role);
          return Result.success();
        }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
       roleService.removeById(id);
          return Result.success();
        }

   @DeleteMapping("/del/batch")
   public Result deleteBatch(@RequestBody List<Integer> ids) {
       roleService.removeByIds(ids);
          return Result.success();
        }

   @GetMapping
   public Result findALL(){
          return Result.success(roleService.list());
        }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));

        }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
        }

    /**
     * Bind the relationship between roles and menus
     * @param roleId Role ID
     * @param menuIds  Array of menu IDs
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success( roleService.getRoleMenu(roleId));
    }
    }

