package com.example.springboot.mapper;

import com.example.springboot.controller.dto.UserPasswordDTO;
import com.example.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * <p>
 *  Mapper interface
 * </p>
 *
 * @author ada
 * @since 2023-05-23
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Update("update sys_user set password = #{newPassword} where username = #{username} and password = #{password} ")
    int updatePassword(UserPasswordDTO userPasswordDTO);

    /**
     * Get by Id
     * @param id
     * @return
     */
    User getById(String id);

    /**
     * Get current user by openid
     * @param openid
     * @return
     */
    User getByOpenId(@Param("openid") String openid);

    /**
     * Count users based on dynamic conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
