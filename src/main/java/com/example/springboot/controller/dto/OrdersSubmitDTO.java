package com.example.springboot.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrdersSubmitDTO implements Serializable {

    private String remark;

    @TableField("number_mesa")
    private Integer numberMesa;

    private BigDecimal amount;


}
