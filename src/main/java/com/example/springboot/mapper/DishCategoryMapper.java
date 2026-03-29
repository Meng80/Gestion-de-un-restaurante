package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.annotation.AutoFill;
import com.example.springboot.entity.DishCategory;
import com.example.springboot.common.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DishCategoryMapper extends BaseMapper<DishCategory> {

    /**
     * Insert dishCategory
     * @param dishCategory
     */
    @AutoFill(OperationType.INSERT)
    int insert(DishCategory dishCategory);

    /**
     * Delete by Id
     * @param id
     */
    void deleteById(@Param("id") Long id);

    /**
     * Update dishCategory By Id
     * @param dishCategory
     */
    @AutoFill(OperationType.UPDATE)
    void update(DishCategory dishCategory);

}

