package com.example.springboot.controller.dto;

import com.example.springboot.entity.OrderDetail;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrdersDTO implements Serializable {

    private Long id;

    private String number;

    // Order status: 1 Pending payment, 2 Pending, 3 Shipping, 4 Completed, 5 Cancelled
    private Integer status;

    private Long userId;

    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    // Payment method: 1 Cash, 2 Card
    private Integer payMethod;

    private BigDecimal amount;

    private String remark;

    private Integer NumMesa;

    private List<OrderDetail> orderDetails;

}
