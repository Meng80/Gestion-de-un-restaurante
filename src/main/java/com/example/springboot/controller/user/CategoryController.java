package com.example.springboot.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.common.StatusConstant;
import com.example.springboot.entity.DishCategory;
import com.example.springboot.service.IDishCategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Api(tags = "Cliente-CategoryController")
public class CategoryController {

    @Autowired
    private IDishCategoryService dishCategoryService;

    @GetMapping("/list")
    public Result list() {
        LambdaQueryWrapper<DishCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishCategory::getStatus, StatusConstant.ENABLE);
        wrapper.orderByAsc(DishCategory::getSort);
        List<DishCategory> list = dishCategoryService.list(wrapper);
        return Result.success(list);
    }
}