package com.example.springboot.mapper;

import com.example.springboot.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    /**
     * Batch insert order detail data
     * @param orderDetailList
     */
    void insertBatch(ArrayList<OrderDetail> orderDetailList);

    /**
     * Order details by order ID
     * @param ordersId
     * @return
     */
    List<OrderDetail> getByOrderId(Long ordersId);
}
