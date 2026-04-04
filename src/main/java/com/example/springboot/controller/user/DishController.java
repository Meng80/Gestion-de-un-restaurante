package com.example.springboot.controller.user;


import com.example.springboot.common.Result;
import com.example.springboot.entity.Dish;
import com.example.springboot.service.IDishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
}
