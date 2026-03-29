package com.example.springboot.controller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DishCategoryDTO implements Serializable {

    private Long id;

    private String name;

    private Integer sort;

    private Integer status;

}

