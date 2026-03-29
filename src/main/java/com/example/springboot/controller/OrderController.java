package com.example.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.controller.dto.OrdersCancelDTO;
import com.example.springboot.controller.dto.OrdersConfirmDTO;
import com.example.springboot.entity.Orders;
import com.example.springboot.service.IOrderService;
import com.example.springboot.vo.OrderStatisticsVO;
import com.example.springboot.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Order control
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Api(tags = "order-Controller")
public class OrderController {

    @Autowired
    private IOrderService orderService;


    /**
     * Count orders by status
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("Count orders by status")
    public Result statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * Detailed information of an order
     * @param id
     * @return
     */
    @GetMapping("/details/{id}")
    @ApiOperation("Detailed information of an order")
    public Result details(@PathVariable("id") Long id) {
        OrderVO details = orderService.details(id);
        return Result.success(details);
    }

    /**
     * Confirm order
     * @param ordersConfirmDTO
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("Confirm order")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }


    /**
     * Cancel order
     * @param ordersCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("Cancel order")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }


    /**
     * Complete order
     * @param id
     * @return
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("Complete order")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }


    /**
     * Deliver order
     * @param request
     * @return
     */
    @PutMapping("/deliver")
    @ApiOperation("Deliver Order")
    public Result deliver(@RequestBody Map<String, Long> request) {
        Long id = request.get("id");
        orderService.deliver(id);
        return Result.success();
    }

    /**
     * Refund order
     * @param request
     * @return
     */
    @PutMapping("/refund")
    @ApiOperation("Refund order")
    public Result refund(@RequestBody Map<String, Object> request) {
        Long id = Long.valueOf(request.get("id").toString());
        String cancelReason = (String) request.get("cancelReason");
        orderService.refund(id, cancelReason);
        return Result.success();
    }

    /**
     * Query orders by page with dynamic conditions
     * @param numberOrder order number
     * @param numberMesa table number
     * @param status order status
     * @param pageNum current page number
     * @param pageSize page size
     * @return paginated order results
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam(required = false) String numberOrder,
                           @RequestParam(required = false) Integer numberMesa,
                           @RequestParam(required = false) Integer status,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (numberOrder != null) {
            queryWrapper.eq("number_order", numberOrder);
        }
        if (numberMesa != null) {
            queryWrapper.eq("number_mesa", numberMesa);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        return Result.success(orderService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }




}
