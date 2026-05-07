package com.example.springboot.controller;


import com.example.springboot.common.Result;
import com.example.springboot.service.IWorkspaceService;
import com.example.springboot.vo.BusinessDataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * WorkSpace
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
@Api(tags = "WorkSpaceController")
public class WorkSpaceController {

    @Autowired
    private IWorkspaceService workspaceService;

    /**
     * Today's BusinessData
     * @return
     */
    @GetMapping("/businessData")
    @ApiOperation("businessData")
    public Result businessData(){
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * Query order management data
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("orderOverView")
    public Result orderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * Query overviewDishes
     * @return
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("dishOverView")
    public Result dishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }

    /**
     * Get turnover statistics
     */
    @GetMapping("/turnover")
    @ApiOperation("Turnover Statistics")
    public Result turnoverStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return Result.success(workspaceService.getTurnover(begin, end));
    }

    /**
     * Order Statistics
     */
    @GetMapping("/orderStatistics")
    @ApiOperation("Order Statistics")
    public Result orderStatistics(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return Result.success(workspaceService.getOrderStatistics(begin, end));
    }

    /**
     * Sales Top 10
     */
    @GetMapping("/top10")
    @ApiOperation("Sales Top 10")
    public Result salesTop10(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        return Result.success(workspaceService.getSalesTop10(begin, end));
    }

}
