package com.example.springboot.controller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrdersConfirmDTO implements Serializable {

    private Long id;
    // Order status: 1 Pending payment, 2 Pending acceptance, 3 Delivering, 4 Completed, 5 Cancelled
    private Integer status;

}
