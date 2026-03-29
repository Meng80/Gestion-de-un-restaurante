package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.BaseContext;
import com.example.springboot.common.MessageConstant;
import com.example.springboot.common.StatusConstant;
import com.example.springboot.controller.dto.DishCategoryDTO;
import com.example.springboot.entity.DishCategory;
import com.example.springboot.exception.DeletionNotAllowedException;
import com.example.springboot.mapper.DishCategoryMapper;
import com.example.springboot.mapper.DishMapper;
import com.example.springboot.service.IDishCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;


@Service
@Slf4j
public class DishCategoryServiceImpl extends ServiceImpl<DishCategoryMapper, DishCategory> implements IDishCategoryService {



    @Autowired
    private DishCategoryMapper dishCategoryMapper;
    @Autowired
    private DishMapper dishMapper;



    /**
     * Insert Category
     *
     * @param dishCategoryDTO
     */
    public void insert(DishCategoryDTO dishCategoryDTO) {

        DishCategory dishCategory = new DishCategory();
        BeanUtils.copyProperties(dishCategoryDTO, dishCategory);
        dishCategory.setStatus(StatusConstant.DISABLE);

        dishCategoryMapper.insert(dishCategory);
    }


    /**
     * Delete by Id
     * @param id
     */
    public void deleteById(Long id) {
        Integer count = dishMapper.countByDishCategoryId(id);
        if(count > 0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        dishCategoryMapper.deleteById(id);
    }



    /**
     * Update Category
     * @param dishCategoryDTO
     */
    public void update(DishCategoryDTO dishCategoryDTO) {
        DishCategory dishCategory = new DishCategory();
        BeanUtils.copyProperties(dishCategoryDTO,dishCategory);
        dishCategoryMapper.update(dishCategory);
    }


    /**
     * Star or stop category
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        DishCategory category = DishCategory.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        dishCategoryMapper.update(category);
    }


}
