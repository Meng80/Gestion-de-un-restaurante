package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.controller.dto.*;
import com.example.springboot.entity.Orders;
import com.example.springboot.vo.OrderStatisticsVO;
import com.example.springboot.vo.OrderVO;

public interface IOrderService extends IService<Orders> {

    /**
     * Details info
     * @param id
     * @return
     */
    OrderVO details(Long id);


    /**
     * Count orders by status
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * Confirm order
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * Refund order
     * @param id
     * @param cancelReason
     */
    void refund(Long id, String cancelReason);

    /**
     * Cancel order
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;


    /**
     * Complete order
     * @param id
     */
    void complete(Long id);


    /**
     * Deliver Order
     * @param id
     */
    void deliver(Long id);
}
