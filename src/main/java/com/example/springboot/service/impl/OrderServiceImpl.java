package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.common.MessageConstant;
import com.example.springboot.controller.dto.*;
import com.example.springboot.entity.*;
import com.example.springboot.exception.OrderBusinessException;
import com.example.springboot.exception.ShoppingCartBusinessException;
import com.example.springboot.mapper.*;
import com.example.springboot.service.IOrderService;
import com.example.springboot.vo.OrderStatisticsVO;
import com.example.springboot.vo.OrderSubmitVO;
import com.example.springboot.vo.OrderVO;
import com.example.springboot.websocket.WebSocketServer;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WebSocketServer webSocketServer;


    /**
     * Query order details
     * @param id
     * @return
     */
    @Override
    public OrderVO details(Long id) {
        Orders orders = orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetailList);

        return orderVO;
    }

    /**
     * Statistics of order counts by status
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        Integer toBeConfirmed = orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        Integer confirmed = orderMapper.countStatus(Orders.CONFIRMED);
        Integer completed = orderMapper.countStatus(Orders.COMPLETED);
        Integer cancelled = orderMapper.countStatus(Orders.CANCELLED);

        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(toBeConfirmed);
        orderStatisticsVO.setConfirmed(confirmed);
        orderStatisticsVO.setCompleted(completed);
        orderStatisticsVO.setCancelled(cancelled);

        return orderStatisticsVO;
    }

    /**
     * Confirm order
     * @param ordersConfirmDTO
     */
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = Orders.builder()
                .id(ordersConfirmDTO.getId())
                .status(Orders.CONFIRMED)
                .build();
        orderMapper.update(orders);
    }


    /**
     * Cancel order
     * @param ordersCancelDTO
     */
    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception {

        Orders orderDB = orderMapper.getById(ordersCancelDTO.getId());

        Orders orders = new Orders();
        orders.setId(ordersCancelDTO.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }


    /**
     * Deliver order - update status to completed
     * @param id
     */
    @Override
    public void deliver(Long id) {
        Orders orderDB = orderMapper.getById(id);
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (!orderDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException("Invalid order status, unable to deliver");
        }

        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * Complete order
     * @param id
     */
    @Override
    public void complete(Long id) {

        Orders orderDB = orderMapper.getById(id);

        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if (!orderDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(id);

        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**Refund order - cancel a completed order, status changes to cancelled
     * @param id
     * @param cancelReason
     */
    @Override
    public void refund(Long id, String cancelReason) {
        Orders orderDB = orderMapper.getById(id);
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (!orderDB.getStatus().equals(Orders.COMPLETED)) {
            throw new OrderBusinessException("Only completed orders can be refunded");
        }

        Orders orders = new Orders();
        orders.setId(id);
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(cancelReason);
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * Convert orders to OrderVO
     * @param page
     * @return
     */
    private List<OrderVO> getOrderVoList(Page<Orders> page) {
        ArrayList<OrderVO> orderVOArrayList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            ordersList.forEach(orders -> {
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);

                String orderDishStr = getOrderDishStr(orders);
                orderVO.setOrderDishes(orderDishStr);
                orderVOArrayList.add(orderVO);
            });
        }
        return orderVOArrayList;
    }

    /**
     * Get the dish information string based on order ID
     * @param orders
     * @return
     */
    private String getOrderDishStr(Orders orders) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        List<String> orderDishList = orderDetailList.stream().map(orderDetail -> orderDetail.getName() + "*" + orderDetail.getNumber() + ";").collect(Collectors.toList());
        return String.join("", orderDishList);
    }

    /**
     * User cancel order by Id
     * @param id
     */
    @Override
    public void userCancelById(Long id) {
        Orders orderDB = orderMapper.getById(id);
        if (orderDB == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (orderDB.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(orderDB.getId());
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("User Cancel");
        orders.setCancelTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    /**
     * User submit Order
     * @param ordersSubmitDTO
     * @return
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {



        Integer tableId = ordersSubmitDTO.getNumberMesa();
        if (tableId == null) {
            throw new ShoppingCartBusinessException("TableId cant be null");
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTableId(Long.valueOf(tableId));


        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setNumberMesa(tableId);
        order.setNumberOrder(String.valueOf(System.currentTimeMillis()));
        order.setStatus(Orders.TO_BE_CONFIRMED);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());
        orderMapper.insert(order);

        ArrayList<OrderDetail> orderDetailList = new ArrayList<>();
        shoppingCartList.forEach(cart->{
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetailList.add(orderDetail);
        });

        orderDetailMapper.insertBatch(orderDetailList);

        shoppingCartMapper.deleteByTableId(Long.valueOf(tableId));

        OrderSubmitVO submitVO = OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumberOrder())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();

        return submitVO;
    }

    /**
     * Get order detail by number order
     * @param numberOrder
     * @return
     */
    @Override
    public OrderVO getByNumberOrder(String numberOrder) {
        Orders orders = orderMapper.getByNumberOrder(numberOrder);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);

        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());
        orderVO.setOrderDetailList(orderDetailList);

        String orderDishStr = getOrderDishStr(orders);
        orderVO.setOrderDishes(orderDishStr);

        return orderVO;
    }


    /**
     * Customer reminder
     * @param id OrderID
     * @return Result reminder
     */
    @Override
    public String reminder(Long id) {
        Orders order = this.getById(id);

        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        if (order.getStatus() != Orders.TO_BE_CONFIRMED && order.getStatus() != Orders.CONFIRMED) {
            throw new OrderBusinessException("Only pending confirmation or preparing orders can be reminded");
        }

        String message = "REMINDER:" + order.getId() + ":" + order.getNumberMesa();
        webSocketServer.sendToAllClient(message);
        log.info("Reminder sent successfully, Order ID: {}, Table Number: {}", order.getId(), order.getNumberMesa());

        return "Reminder sent successfully, restaurant has been notified";
    }

}

