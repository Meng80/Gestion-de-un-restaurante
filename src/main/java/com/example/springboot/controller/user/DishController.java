package com.example.springboot.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Dish;
import com.example.springboot.service.IDishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Api(tags = "Cliente-DishController")
public class DishController {

    @Autowired
    private IDishService dishService;

    /**
     * Get dish list by category id (only on-sale dishes)
     * @param categoryId category id
     * @return list of dishes with status=1
     */
    /**
     * Get dish list by category id (only on-sale dishes)
     * @param categoryId category id
     * @return list of dishes with status=1
     */
    @GetMapping("/list")
    @ApiOperation("Dish list by CategoryId")
    public Result list(@RequestParam Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * Buscar platos por nombre
     * GET /user/dish/search?keyword=pizza
     */
    @GetMapping("/search")
    @ApiOperation("Buscar platos por nombre")
    public Result search(
            @ApiParam(value = "Palabra clave para buscar", required = true)
            @RequestParam String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return Result.success();
        }
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(Dish::getName, keyword.trim())
                .eq(Dish::getStatus, 1);

        List<Dish> dishList = dishService.list(queryWrapper);
        return Result.success(dishList);
    }
}
