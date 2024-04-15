package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author ada
 * @since 2023-06-22
 */
@Getter
@Setter
@TableName("sys_menu")
@ApiModel(value = "Menu", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("name")
      private String name;

      @ApiModelProperty("path")
      private String path;

      @ApiModelProperty("icon")
      private String icon;

      @ApiModelProperty("description")
      private String description;

      @TableField(exist = false)
      private List<Menu> children;

      private Integer pid;

      private String pagePath;


}
