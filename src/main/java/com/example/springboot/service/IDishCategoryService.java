package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.controller.dto.DishCategoryDTO;
import com.example.springboot.entity.DishCategory;


public interface IDishCategoryService extends IService<DishCategory> {
    /**
     * Insert
     * @param dishCategoryDTO
     */
    void insert(DishCategoryDTO dishCategoryDTO);

    /**
     * DeleteById
     * @param id
     */
    void deleteById(Long id);

    /**
     * Update Category
     * @param dishCategoryDTO
     */
    void update(DishCategoryDTO dishCategoryDTO);

    /**
     * Enable/Disable category
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

}
