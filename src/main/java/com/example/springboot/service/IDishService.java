package com.example.springboot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.controller.dto.DishDTO;
import com.example.springboot.entity.Dish;

import java.util.List;

public interface IDishService extends IService<Dish> {

    /**
     * Save dish with flavor
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);



    /**
     * Delete dish
     * @param id
     */
    void delete(Long id);


    /**
     * Query dishes by category id
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);



    /**
     * Update dish sales status
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
