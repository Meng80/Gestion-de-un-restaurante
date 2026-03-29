package com.example.springboot.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.DishDTO;
import com.example.springboot.entity.Dish;
import com.example.springboot.service.IDishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;



/**
* Dish Controller
*/
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "dish-controller")
@Slf4j
public class DishController {

    @Autowired
    private IDishService dishService;


    /**
     * Add dish
     * @param dishDTO
     * @return
     */
    @PostMapping()
    @ApiOperation("Add dish")
    public Result save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }


    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) Integer status,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }

        queryWrapper.inSql("category_id",
                "SELECT id FROM dish_category WHERE status = 1");

        Page<Dish> page = dishService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return Result.success(page);
    }


    /**
     * Delete dish
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation("Delete Dish")
    public Result deleteById(@PathVariable Long id){
        dishService.delete(id);
        return Result.success();
    }


    /**
     * Delete dish by batch
     * @param ids
     * @return
     */
    @DeleteMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        dishService.removeByIds(ids);
        return Result.success();
    }


    /**
     * Update dish
     *
     * @param dishDTO
     * @return
     */
    @PutMapping
    @ApiOperation("update dish")
    public Result update(@RequestBody DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishService.updateById(dish);
        return Result.success();
    }



    /**
     * Query dishes by dishCategory id (only returns dishes with enabled status)
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("Find dish by categoryId")
    public Result list(@RequestParam(required = false) Long categoryId) {
        QueryWrapper<Dish> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("status", 1);
        if (categoryId != null) {
            queryWrapper.eq("category_id", categoryId);
        }

        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }


    /**
     * StartOrStop dish
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("star or stop dish")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        dishService.startOrStop(status, id);
        return Result.success();
    }

}
