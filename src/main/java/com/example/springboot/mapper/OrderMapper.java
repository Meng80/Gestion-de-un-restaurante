package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.controller.dto.GoodsSalesDTO;
import com.example.springboot.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

    /**
     * Inter Order data
     * @param order
     */
    int insert(Orders order);

    /**
     * Get by numberOrder and numberMesa
     * @param numberOrder
     * @param numberMesa
     * @return
     */
    Orders getByNumberAndNumberMesa(@Param("numberOrder") String numberOrder, @Param("number_mesa") Integer numberMesa);

    /**
     * Update order info
     * @param orders
     */
    void update(Orders orders);


    /**
     * Get order by id
     * @param id
     * @return
     */
    Orders getById(Long id);

    /**
     * Count order by status
     * @param status
     * @return
     */
    Integer countStatus(Integer status);

    /**
     * Get order by status and orderTime
     * @param status
     * @param orderTime
     */
    List<Orders> getByStatusAndOrderTime(@Param("status") Integer status, @Param("orderTime") LocalDateTime orderTime);

    /**
     * Calculate turnover based on dynamic conditions
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * Count orders based on dynamic conditions
     * @param map
     * @return
     */
    Integer countByMap(Map map);

    /**
     * Get product sales ranking
     * @param begin
     * @param end
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
