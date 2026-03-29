package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.annotation.AutoFill;
import com.example.springboot.entity.Dish;
import com.example.springboot.common.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface DishMapper extends BaseMapper<Dish> {

        /**
         * CountByDishCategoryId
         * @param dishCategoryId
         * @return
         */
        Integer countByDishCategoryId(@Param("dishCategoryId") Long dishCategoryId);

        /**
         * Insert dish
         * @param dish
         */
        @AutoFill(OperationType.INSERT)
        int insert(Dish dish);

        /**
         * GetById
         * @param id
         * @return
         */
        Dish getById(Long id);

        /**
         * DeleteById
         * @param id
         */
        void deleteById(Long id);

        /**
         * Update
         * @param dish
         */
        @AutoFill(OperationType.UPDATE)
        int update(Dish dish);

        /**
         * List
         * @param dish
         * @return
         */
        List<Dish> list(Dish dish);


        /**
         * CountByMap
         * @param map
         * @return
         */
        Integer countByMap(Map map);

    }
