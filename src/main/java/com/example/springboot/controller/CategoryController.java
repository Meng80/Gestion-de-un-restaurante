package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Category;
import com.example.springboot.service.ICategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private ICategoryService categoryService;

    @PutMapping
    public Result save(@RequestBody Category category){
        categoryService.saveOrUpdate(category);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        categoryService.removeById(id);
        return Result.success();
    }

    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        categoryService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findALL(){
        return Result.success(categoryService.list());

    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(categoryService.getById(id));

    }
    @GetMapping("/list")
    public Result list(){
        List list = categoryService.list();
        return Result.success(list);
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        return Result.success(categoryService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}
