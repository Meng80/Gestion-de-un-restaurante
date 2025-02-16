package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("sys_record")
@ApiModel(value = "Record", description = "Record entity")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("product")
    private String product;

    @ApiModelProperty("count")
    private Integer count;

    @ApiModelProperty("userID")
    private Integer userId;

    @ApiModelProperty("createTime")
    private LocalDateTime createTime;

    @ApiModelProperty("mark")
    private String mark;

    @TableField(exist = false)
    private String action;
}
