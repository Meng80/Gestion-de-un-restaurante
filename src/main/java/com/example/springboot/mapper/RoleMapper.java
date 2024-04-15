package com.example.springboot.mapper;

import com.example.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper interface
 * </p>
 *
 * @author ada
 * @since 2023-06-25
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where mark = #{mark}")
    Integer selectByFlag(@Param("mark") String mark);
}
