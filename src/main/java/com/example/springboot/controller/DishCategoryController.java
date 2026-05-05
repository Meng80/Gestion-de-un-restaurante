package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.MessageConstant;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.DishCategoryDTO;
import com.example.springboot.entity.DishCategory;
import com.example.springboot.exception.DeletionNotAllowedException;
import com.example.springboot.mapper.DishMapper;
import com.example.springboot.service.IDishCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/admin/category")
@Api(tags = "dishCategory-Controller")
@Slf4j
public class DishCategoryController {

    @Autowired
    private IDishCategoryService dishCategoryService;

    @Autowired
    private DishMapper dishMapper;

    @GetMapping("/list")
    public Result list(@RequestParam(required = false) Integer status){
        QueryWrapper<DishCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort").orderByDesc("id");

        if (status != null) {
            queryWrapper.eq("status", status);
        }

        List<DishCategory> list = dishCategoryService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<DishCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByAsc("sort").orderByDesc("id");
        return Result.success(dishCategoryService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    /**
     * Delete category
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("Delete Category")
    public Result deleteById(@PathVariable Long id){
        dishCategoryService.deleteById(id);
        return Result.success();
    }

    /**
     * Delete category by batch
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Long> ids) {
        for (Long id : ids) {
            Integer count = dishMapper.countByDishCategoryId(id);
            if (count > 0) {
                throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
            }
        }
        dishCategoryService.removeByIds(ids);
        return Result.success();
    }
    /**
     * Update category
     * @param dishCategoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("Update Category")
    public Result update(@RequestBody DishCategoryDTO dishCategoryDTO){
        dishCategoryService.update(dishCategoryDTO);
        return Result.success();
    }

    /**
     * Star or stop status
     * @param status
     * @param id
     * @return
     */
    @PutMapping("/status/{status}")
    @ApiOperation("StartOrStop Category")
    public Result startOrStop(@PathVariable("status") Integer status, @RequestParam Long id){
        dishCategoryService.startOrStop(status,id);
        return Result.success();
    }

    @PostMapping
    public Result save(@RequestBody DishCategoryDTO dishCategoryDTO){
        dishCategoryService.insert(dishCategoryDTO);
        return Result.success(dishCategoryDTO);
    }
}
