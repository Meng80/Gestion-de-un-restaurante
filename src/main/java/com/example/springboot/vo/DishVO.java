package com.example.springboot.vo;

import com.example.springboot.entity.Dish;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DishVO extends Dish {
    private Integer categoryStatus;
    private String categoryName;
}