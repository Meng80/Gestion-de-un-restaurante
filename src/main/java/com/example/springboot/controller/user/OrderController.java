package com.example.springboot.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.OrdersSubmitDTO;
import com.example.springboot.entity.Orders;
import com.example.springboot.service.IOrderService;
import com.example.springboot.vo.OrderSubmitVO;
import com.example.springboot.vo.OrderVO;
import com.example.springboot.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "Cliente-OrderController")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * User submit order
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("User submit order")
    public Result submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("User submit order：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);
        webSocketServer.sendToAllClient("NEW_ORDER:" + ordersSubmitDTO.getNumberMesa());
        return Result.success(orderSubmitVO);
    }

    /**
     * Order details
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("Order details")
    public Result details(@PathVariable("id") Long id) {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    /**
     * User cancel order
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("User cancel order")
    public Result cancel(@PathVariable Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * Get order detail by number order
     * @param numberOrder
     * @return
     */
    @GetMapping("/search")
    @ApiOperation("Get order detail by number order")
    public Result searchByNumberOrder(@RequestParam String numberOrder) {
        log.info("Get order detail by number order：{}", numberOrder);
        OrderVO orderVO = orderService.getByNumberOrder(numberOrder);
        return Result.success(orderVO);
    }

    /**
     * Get orders by table number (history)
     * @param tableId table number
     * @param pageNum page number (default 1)
     * @param pageSize page size (default 10)
     * @return paginated order list
     */
    @GetMapping("/list")
    @ApiOperation("Get orders by table number")
    public Result getOrdersByTable(@RequestParam Integer tableId,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("Get orders by tableId: {}, pageNum: {}, pageSize: {}", tableId, pageNum, pageSize);

        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("number_mesa", tableId);
        queryWrapper.orderByDesc("id");

        Page<Orders> page = orderService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return Result.success(page);
    }


    /**
     * Customer reminder
     * @param id OrderID
     * @return
     */
    @PostMapping("/reminder/{id}")
    @ApiOperation("Customer reminder to hurry up")
    public Result reminder(@PathVariable Long id) {
        log.info("Customer reminder，OrderID：{}", id);
        String result = orderService.reminder(id);
        return Result.success(result);
    }
}


