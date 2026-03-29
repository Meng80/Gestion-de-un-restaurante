package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.StatusConstant;
import com.example.springboot.controller.dto.DishDTO;
import com.example.springboot.entity.Dish;
import com.example.springboot.mapper.DishMapper;
import com.example.springboot.service.IDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements IDishService {

    @Autowired
    private DishMapper dishMapper;



    /**
     * Sava with flavor
     * @param dishDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
    }


    /**
     * 菜品删除
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        dishMapper.deleteById(id);

    }


    /**
     * Query dish by id
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }


    /**
     * Start or Stop dish
     * @param status
     * @param id
     */
    @Override
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

}
