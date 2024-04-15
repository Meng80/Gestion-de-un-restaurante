package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author ada
 * @since 2023-05-23
 */

@Data
@TableName("sys_user")
@ApiModel(value = "User", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("username")
      private String username;

      @ApiModelProperty("password")
      private String password;

      @ApiModelProperty("nickname")
      private String nickname;

      @ApiModelProperty("email")
      private String email;

      @ApiModelProperty("phone")
      private String phone;

      @ApiModelProperty("address")
      private String address;

      @ApiModelProperty("createTime")
      private Date createTime;

      @ApiModelProperty("avatarUrl")
      private String avatarUrl;

      @ApiModelProperty("role")
      private String role;




}
