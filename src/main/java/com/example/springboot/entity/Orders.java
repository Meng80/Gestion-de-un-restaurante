package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {

    /**
     *  Order status: 1 Pending payment, 2 Pending, 3 Shipping, 4 Completed, 5 Cancelled
     */
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2; //pending
    public static final Integer CONFIRMED = 3; // shipping
    public static final Integer COMPLETED = 4;
    public static final Integer CANCELLED = 5;

    /**
     * Payment status: 0 Unpaid, 1 Paid, 2 Refunded
     */
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;

    private static final long serialVersionUID = 1L;

    private Long id;

    @TableField("number_order")
    private String numberOrder;

    //Order status: 1 Pending payment, 2 Pending, 3 Shipping, 4 Completed, 5 Cancelled
    private Integer status;

    @TableField("user_name")
    private String userName;

    //Payment method: 1 Cash, 2 Card
    @TableField("pay_method")
    private Integer payMethod;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("order_time")
    private LocalDateTime orderTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("checkout_time")
    private LocalDateTime checkoutTime;

    //Payment status: 0 Unpaid, 1 Paid, 2 Refunded
    @TableField("pay_status")
    private Integer payStatus;


    private BigDecimal amount;


    private String remark;


    @TableField("number_mesa")
    private Integer numberMesa;


    @TableField("cancel_reason")
    private String cancelReason;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("cancel_time")
    private LocalDateTime cancelTime;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("delivery_time")
    private LocalDateTime deliveryTime;

}
