package com.example.springboot.controller.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;

    private Integer tableId;

    private Integer number;
}
